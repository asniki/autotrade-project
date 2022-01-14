package com.autotrade.connector.component;

import com.autotrade.connector.enums.LogLevel;
import com.autotrade.connector.exception.ConnectorWrapperException;
import com.autotrade.connector.exception.SendCommandException;
import com.autotrade.connector.model.ConnectionProfile;
import com.autotrade.connector.model.callback.*;
import com.autotrade.connector.model.command.Command;
import com.autotrade.connector.model.command.Connect;
import com.autotrade.connector.model.command.Disconnect;
import com.autotrade.connector.model.command.GetConnectorVersion;
import com.autotrade.connector.model.response.Error;
import com.autotrade.connector.model.response.Result;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.springframework.stereotype.Component;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.GroupedFlux;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * Версия 6.24 билд 2.21.14
 * от 20.09.2021
 */
//@Component
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
            "server_status", ServerStatus2.class
//            "connector_version", ConnectorVersion.class
    );

    public ConnectorWrapperFlux(Utils utils, ObjectMapper objectMapper, DataContext dataContext) throws ConnectorWrapperException, ParserConfigurationException {

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
        Map<String, Class<? extends Callback>> callbackTypes = Map.of(
                "server_status", ServerStatus2.class
        );

        // мапа содержит списки подписчиков на потоки определенных по ключу объектов
        Map<String, List<Consumer<? super Callback>>> callbackConsumers = Map.of(
                "server_status", List.of(dataContext::onServerStatusCallback)
        );

        // через украденную ссылку handler колбек будет класть объекты в поток
        Flux<Pointer> flux = Flux
                .<Pointer>create(emitter -> {
                    handler = emitter;
                    }, FluxSink.OverflowStrategy.BUFFER) //TODO or some other overflow strategy
                .log();

        // чтобы поток не сразу потек, а дождался всех подписчиков, оборачиваем в ConnectableFlux
        ConnectableFlux<Pointer> connectableFlux = flux.publish();

        connectableFlux
                .map(pData -> {
                    String xmlData = utils.pointerToString(pData);
                    freeMemory(pData);
                    String rootName = utils.getRootElementName(xmlData);
                    return callbackTypes.containsKey(rootName)
                            ? utils.deserializeCallbackFlux(xmlData, callbackTypes.get(rootName))
                            : new Callback(rootName);
                })
                .log();

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
        connectableFlux.connect();


        //TODO получить Map<String, ConnectableFlux<Callback>> и подписываться по ключу на соответствующие флаксы - фантазия


    }

//    public ConnectableFlux<Pointer> getConnectableFlux() {
//        return connectableFlux;
//    }

//    public void handleCallback(Pointer pData) {
//        handler.next(pData);
//    }

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

    public <T extends Command, V extends Result> V sendCommand(T command, Class<V> clazz) throws ConnectorWrapperException {
        try {
            String commandString = utils.serializeCommand(command);
            Pointer commandPtr = utils.stringToPointer(commandString);
            log.info("SendCommand: {}", commandString);
            Pointer responsePtr = txmlConnector.SendCommand(commandPtr);
            String responseString = utils.pointerToString(responsePtr);
            freeMemory(responsePtr);
            log.info("SendCommand response: {}", responseString);
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

    public Result connect() throws ConnectorWrapperException, IOException {
//        ConnectionProfile connectionProfile = utils.getConnectionProfile();
        ConnectionProfile connectionProfile = utils.getDemoConnectionProfile();

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

    public Result getConnectorVersion() throws ConnectorWrapperException {
        GetConnectorVersion getConnectorVersion = new GetConnectorVersion();
        return sendCommand(getConnectorVersion, Result.class);
    }

    public Result disconnect() throws ConnectorWrapperException {
        Disconnect disconnect = new Disconnect();
        return sendCommand(disconnect, Result.class);
    }

    private void freeMemory(Pointer pData) {
        boolean freeMemory = txmlConnector.FreeMemory(pData);
        if(!freeMemory) {
            log.error("error while FreeMemory call");
        }
    }

    public static class StringPublisher implements Publisher<String> {

        private Subscriber<String> subscriber;

        @Override
        public void subscribe(Subscriber<? super String> subscriber) {
            this.subscriber = (Subscriber<String>) subscriber;
        }

        public void publish(String str) {
            subscriber.onNext(str);
        }
    };
}
