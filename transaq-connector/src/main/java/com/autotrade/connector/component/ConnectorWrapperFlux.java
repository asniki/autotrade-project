package com.autotrade.connector.component;

import com.autotrade.connector.enums.LogLevel;
import com.autotrade.connector.exception.ConnectorWrapperException;
import com.autotrade.connector.exception.SendCommandException;
import com.autotrade.connector.model.ConnectionProfile;
import com.autotrade.connector.model.callback.*;
import com.autotrade.connector.model.command.*;
import com.autotrade.connector.model.response.Error;
import com.autotrade.connector.model.response.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.*;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Версия 6.24 билд 2.21.14
 * от 20.09.2021
 */
@Component
@Slf4j
public class ConnectorWrapperFlux {

    private final TXMLConnector64 txmlConnector;
//    private final ExecutorService executorService;

    private final Utils utils;
    private final ObjectMapper objectMapper;
    private final DataContext dataContext;

    /** Protect callback from GC */
    private TXMLConnector64.TCallback callbackHolder;

    private FluxSink<Pointer> handler;
    private Flux<Pointer> flux;

    private final Map<String, Class<? extends Callback>> callbackTypes2 = Map.of(
//            "error", Error.class,
//            "authentication", Authentication.class,
//            "markets", Markets.class,
//            "boards", Boards.class,
//            "candlekinds", Candlekinds.class,
//            "securities", Securities.class,
//            "pits", Pits.class,
//            "sec_info_upd", SecurityInfoUpdate.class,
//            "client", Client.class,
//            "positions", Positions.class,
//            "overnight", Overnight.class,
//            "messages", Messages.class,
            "server_status", ServerStatus.class
//            "connector_version", ConnectorVersion.class
    );

    private final List<String> forbiddenToLog;

    @SneakyThrows
    public ConnectorWrapperFlux(Utils utils, ObjectMapper objectMapper, DataContext dataContext) {

        // com.fasterxml.jackson.databind.exc.MismatchedInputException: Cannot construct instance of `com.autotrade.connector.model.callback.ConnectorVersion2
//        ConnectorVersion connectorVersion = null;
//        ConnectorVersion2 connectorVersion2 = null;
//        try {
//            connectorVersion = utils.getXmlMapper().readValue("<connector_version>6.24.2.21.14</connector_version>", ConnectorVersion.class);
//            connectorVersion2 = utils.getXmlMapper().readValue("<connector_version>6.24.2.21.14</connector_version>", ConnectorVersion2.class);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        log.info(connectorVersion2.getVersion());


        forbiddenToLog = Stream.of(utils.getConnectionProfile().getPassword(), utils.getDemoConnectionProfile().getPassword()).filter(Objects::nonNull).collect(Collectors.toList());

        txmlConnector = Native.load("txmlconnector64.dll", TXMLConnector64.class);
        if(txmlConnector == null) {
            throw new ConnectorWrapperException("TXMLConnector64 object not created");
        }
//        executorService = Executors.newCachedThreadPool();

        this.utils = utils;
        this.objectMapper = objectMapper;
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.dataContext = dataContext;


//        Map<String, List<Consumer<? super Callback>>> callbackConsumers = Map.of(
//                "server_status", List.of(callback -> {
//                    try { objectMapper.writeValueAsString(callback); } catch (JsonProcessingException ignored) { }
//                })
//        );

        // мапа для десериализации
        final Map<String, Class<? extends Callback>> callbackTypes = new HashMap<>();
        callbackTypes.put("error", Error.class);
        callbackTypes.put("authentication", Authentication.class);
        callbackTypes.put("markets", Markets.class);
        callbackTypes.put("boards", Boards.class);
        callbackTypes.put("candlekinds", Candlekinds.class);
        callbackTypes.put("securities", Securities.class);
        callbackTypes.put("pits", Pits.class);
        callbackTypes.put("sec_info_upd", SecurityInfoUpdate.class);
        callbackTypes.put("client", Client.class);
        callbackTypes.put("positions", Positions.class);
        callbackTypes.put("overnight", Overnight.class);
        callbackTypes.put("messages", Messages.class);
        callbackTypes.put("server_status", ServerStatus.class);
        callbackTypes.put("connector_version", ConnectorVersion.class);
        callbackTypes.put("current_server", CurrentServer.class);

        // мапа содержит списки подписчиков на потоки определенных по ключу объектов
        final Map<String, List<Consumer<? super Callback>>> callbackConsumers = new HashMap<>();
        callbackConsumers.put("error", List.of(dataContext::onErrorCallback));
        callbackConsumers.put("authentication", List.of(dataContext::onAuthenticationCallback));
        callbackConsumers.put("markets", List.of(dataContext::onMarketsCallback));
        callbackConsumers.put("boards", List.of(dataContext::onBoardsCallback));
        callbackConsumers.put("candlekinds", List.of(dataContext::onCandlekindsCallback));
        callbackConsumers.put("securities", List.of(dataContext::onSecuritiesCallback));
        callbackConsumers.put("pits", List.of(dataContext::onPitsCallback));
        callbackConsumers.put("sec_info_upd", List.of(dataContext::onSecurityInfoUpdateCallback));
        callbackConsumers.put("client", List.of(dataContext::onClientCallback));
        callbackConsumers.put("positions", List.of(dataContext::onPositionsCallback));
        callbackConsumers.put("overnight", List.of(dataContext::onOvernightCallback));
        callbackConsumers.put("messages", List.of(dataContext::onMessagesCallback));
        callbackConsumers.put("server_status", List.of(dataContext::onServerStatusCallback, c -> log.info("second server_status subscriber")));
        callbackConsumers.put("connector_version", List.of(dataContext::onConnectorVersionCallback));
        callbackConsumers.put("current_server", List.of(dataContext::onCurrentServer));

        // через украденную ссылку handler колбек будет класть объекты в поток
        Flux<Callback> flux = Flux
                .<Pointer>create(emitter -> {
                    handler = emitter;
                    }, FluxSink.OverflowStrategy.BUFFER) //TODO or some other overflow strategy
//                .log("OriginalPublisher.")
                .publishOn(Schedulers.parallel())
                .map(pData -> {
                    String xmlData = utils.pointerToString(pData);
                    freeMemory(pData);
                    String rootName = utils.getRootElementName(xmlData);

//                    if(rootName.equals("client")) {
//                        log.info("rootName: " + rootName + "; client: " + xmlData + "; callbackType: " + callbackTypes.get(rootName));
//                        log.info("client object: " + utils.deserializeCallbackFlux(xmlData, callbackTypes.get(rootName)).toString());
//                    }

                    return callbackTypes.containsKey(rootName)
                            ? utils.deserializeCallbackFlux(xmlData, callbackTypes.get(rootName))
                            : new Callback(rootName);
                })
                .doOnError(error -> log.error("Initial flux error", error))
//                .log("ParsedObject.")
                .publish()
                .autoConnect(0);



//        final Map<String, Flux<Callback>> fluxByType = new HashMap<>();
//        fluxByType.put("error", flux.filter(c -> c.getType().equals("error")));
//        fluxByType.put("authentication", flux.filter(c -> c.getType().equals("authentication")));
//        fluxByType.put("markets", flux.filter(c -> c.getType().equals("markets")));
//        fluxByType.put("boards", flux.filter(c -> c.getType().equals("boards")));
//        fluxByType.put("candlekinds", flux.filter(c -> c.getType().equals("candlekinds")));
//        fluxByType.put("securities", flux.filter(c -> c.getType().equals("securities")));
//        fluxByType.put("pits", flux.filter(c -> c.getType().equals("pits")));
//        fluxByType.put("sec_info_upd", flux.filter(c -> c.getType().equals("sec_info_upd")));
//        fluxByType.put("client", flux.filter(c -> c.getType().equals("client")));
//        fluxByType.put("positions", flux.filter(c -> c.getType().equals("positions")));
//        fluxByType.put("overnight", flux.filter(c -> c.getType().equals("overnight")));
//        fluxByType.put("messages", flux.filter(c -> c.getType().equals("messages")));
//        fluxByType.put("server_status", flux.filter(c -> {
//            log.info("filter by type server_status");
//            return c.getType().equals("server_status");
//        }));
//        fluxByType.put("connector_version", flux.filter(c -> c.getType().equals("connector_version")));


        callbackConsumers.keySet().forEach(key -> {
//            if(key.equals("client"))
//                log.info("subscribe to client");
//
//            if(key.equals("server_status"))
//                log.info("subscribe to server_status");

            Flux<Callback> filter = flux
                    .filter(c -> {
//                        if(key.equals("client"))
//                            log.info("filter by type client");
//                        if(key.equals("server_status"))
//                            log.info("filter by type server_status");

                        //TODO
//                        if(c.getType().equals("client")) {
//                            log.info("filtered client: " + c);
//                            log.info("key: " + key + "; c.getType().equals(key) = " + c.getType().equals(key));
//                        }
//
//                        if(c.getType().equals("server_status")) {
//                            log.info("filtered server_status: " + c);
//                            log.info("key: " + key + "; c.getType().equals(key) = " + c.getType().equals(key));
//                        }

                        return c.getKind().equals(key);
                    })
                    .log("filter_" + key + ".", Level.INFO, SignalType.ON_SUBSCRIBE)
                    .doOnError(error -> log.error("Filtered flux error; callback kind: " + key, error));
//            callbackConsumers.get(key).forEach(filter::subscribe);
            callbackConsumers.get(key).forEach(consumer -> filter.subscribe(consumer,
                    error -> log.error("Filtered flux subscriber error; callback kind: " + key, error)));
        });

        flux
                .filter(c -> !callbackConsumers.containsKey(c.getKind()))
                .subscribe(c -> log.warn("Unhandled callback: " + c.getKind()));

//        callbackConsumers.keySet().forEach(key -> {
//            callbackConsumers.get(key).forEach(fluxByType.get(key)::subscribe);
//        });

        // чтобы поток не сразу потек, а дождался всех подписчиков, оборачиваем в ConnectableFlux
//        ConnectableFlux<Pointer> connectableFlux = flux.publish();
//
//        connectableFlux
//                .map(pData -> {
//                    String xmlData = utils.pointerToString(pData);
//                    freeMemory(pData);
//                    String rootName = utils.getRootElementName(xmlData);
//                    return callbackTypes.containsKey(rootName)
//                            ? utils.deserializeCallbackFlux(xmlData, callbackTypes.get(rootName))
//                            : new Callback(rootName);
//                })
//                .log();

//        connectableFlux
////        flux
//                // мапим нативные указатели в нормальные объекты
//                .map(pData -> {
//                    String xmlData = utils.pointerToString(pData);
//                    freeMemory(pData);
//                    String rootName = utils.getRootElementName(xmlData);
//                    return callbackTypes.containsKey(rootName)
//                            ? utils.deserializeCallbackFlux(xmlData, callbackTypes.get(rootName))
//                            : new Callback(rootName);
//                })
//                .log()
//                // группируем по типу в Flux<GroupedFlux<String, Callback>>
//                .groupBy(Callback::getType)
//                // на поток каждой группы подписываем всех подписчиков по ключу
//                .flatMap(groupedFlux -> {
//                    if(callbackConsumers.containsKey(groupedFlux.key()))
//                        callbackConsumers.get(groupedFlux.key()).forEach(groupedFlux::subscribe);
//                    else
//                        log.info("unhandled callback: " + groupedFlux.key());
//                    return groupedFlux;
//                })
//                .then();//TODO тут наверно нужна терминальная операция чтобы триггернуть предыдущие

        // запускаем течение
//        connectableFlux.connect();


        //TODO получить Map<String, ConnectableFlux<Callback>> и подписываться по ключу на соответствующие флаксы - фантазия


    }

//    public ConnectableFlux<Pointer> getConnectableFlux() {
//        return connectableFlux;
//    }

//    public void handleCallback(Pointer pData) {
//        handler.next(pData);
//    }


    /** SetCallback API */
    public void setCallback() throws ConnectorWrapperException {
        TXMLConnector64.TCallback callback = new TXMLConnector64.TCallback() {
            @Override
            public boolean callback(Pointer pData) {
                handler.next(pData); //TODO можно ли тут сразу вызвать метод handler'а
//                handleCallback(pData);
                return true;
            }
        };

        boolean setCallback = txmlConnector.SetCallback(callback);
        if(!setCallback) {
            throw new ConnectorWrapperException("Native method SetCallback returned false");
        }

        callbackHolder = callback;
    }

    /** FreeMemory API */
    private void freeMemory(Pointer pData) {
        boolean freeMemory = txmlConnector.FreeMemory(pData);
        if(!freeMemory) {
            log.error("error while FreeMemory call");
        }
    }

    /** Initialize API */
    public void initialize() throws ConnectorWrapperException {
        Path logsPath = Paths.get("logs").toAbsolutePath();
        try {
            Files.createDirectories(logsPath);
        } catch (IOException e) {
            throw new ConnectorWrapperException("Failed to create logs directory", e);
        }
        Pointer pathPtr = utils.stringToPointer(logsPath.toString());
        Pointer initializePtr = txmlConnector.Initialize(pathPtr, LogLevel.MAXIMUM.value);
        String initialize = "0";

        try {
            if(initializePtr != Pointer.NULL) {
                initialize = utils.pointerToString(initializePtr);
                throw new ConnectorWrapperException(initialize);
            }
        }
        finally {
            log.info("Initialize result: {}", initialize);
            // Native.free(Pointer.nativeValue(initialize));
            freeMemory(initializePtr);
        }
    }

    /** UnInitialize API */
    public void uninitialize() throws ConnectorWrapperException {
        Pointer unInitializePtr = txmlConnector.UnInitialize();
        String unInitialize = "0";
        try {
            if(unInitializePtr != Pointer.NULL) {
                unInitialize = utils.pointerToString(unInitializePtr);
                throw new ConnectorWrapperException(unInitialize);
            }
        }
        finally {
            log.info("UnInitialize result: {}", unInitialize);
//            Native.free(Pointer.nativeValue(unInitialize));
            freeMemory(unInitializePtr);
        }
    }

    /** SendCommand API */
    public <T extends Command, V extends Result> V sendCommand(T command, Class<V> clazz) throws ConnectorWrapperException {
        try {
            String commandString = utils.serializeCommand(command);
            Pointer commandPtr = utils.stringToPointer(commandString);
            if(command instanceof Connect) {
                String toLog = commandString;
                for(var f : forbiddenToLog)
                    toLog = toLog.replace(f, "*");
                log.info(">> {}: {}", command.getId(), toLog);
            }
            else {
                log.info(">> {}: {}", command.getId(), commandString);
            }

            Pointer responsePtr = txmlConnector.SendCommand(commandPtr);
            String responseString = utils.pointerToString(responsePtr);
            freeMemory(responsePtr);
            log.info("<< {}: {}", command.getId(), responseString);
            String responseRootElement = utils.getRootElementName(responseString);
            switch (responseRootElement) {
                case "error":
                    Error error = utils.deserializeCallback(responseString, Error.class);
                    throw new SendCommandException("Command " + command.getId() + " error: " + error.getMessage());
                case "result":
                    return utils.deserializeCallback(responseString, clazz);
                default:
                    throw new UnsupportedOperationException("Unexpected element name: " + responseRootElement);
            }
        }
        catch (Exception e) {
            throw new ConnectorWrapperException("Failed to send command " + command.getId(), e);
        }
    }

    /** Command connect */
    public Result connect() throws ConnectorWrapperException, IOException {
        ConnectionProfile connectionProfile = utils.getConnectionProfile();
//        ConnectionProfile connectionProfile = utils.getDemoConnectionProfile();

        Connect connect = Connect.builder()
                .login(connectionProfile.getLogin())
                .password(connectionProfile.getPassword())
                .host(connectionProfile.getHost())
                .port(connectionProfile.getPort())
                .autoPositions(true)
                .milliseconds(true)
                .requestDelay(100)
                .sessionTimeout(30)
                .requestTimeout(10)
                .build();

        return sendCommand(connect, Result.class);
    }

    /** Command disconnect */
    public Result disconnect() throws ConnectorWrapperException {
        Disconnect disconnect = new Disconnect();
        return sendCommand(disconnect, Result.class);
    }

    /** Command get_connector_version */
    public Result getConnectorVersion() throws ConnectorWrapperException {
        GetConnectorVersion getConnectorVersion = new GetConnectorVersion();
        return sendCommand(getConnectorVersion, Result.class);
    }

    /** Command server_status */
    public Result getServerStatus() throws ConnectorWrapperException {
        GetServerStatus getServerStatus = new GetServerStatus();
        return sendCommand(getServerStatus, Result.class);
    }

    /** Command get_securities */
    public Result getSecurities() throws ConnectorWrapperException {
        GetSecurities getSecurities = new GetSecurities();
        return sendCommand(getSecurities, Result.class);
    }

    /** Command get_server_id */
    public Result getServerId() throws ConnectorWrapperException {
        GetServerId getServerId = new GetServerId();
        return sendCommand(getServerId, Result.class);
    }
}
