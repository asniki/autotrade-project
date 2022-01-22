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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Processor;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.SubmissionPublisher;
import java.util.function.Consumer;
import java.util.function.Function;

//TODO e.printStackTrace() to log stacktrace
//TODO как кидать эксепшены в лямбдах и тредах - треды в принципе несовместимы с checked эксепшенами?
// при написании методов, которые наследуют checked эксепшены от библиотек, нужно сразу конвертировать их в RuntimeException, если планируется вызывать методы в многопотоке?

/**
 * Версия 6.24 билд 2.21.14
 * от 20.09.2021
 */
//@Component
@Slf4j
public class ConnectorWrapper {
//
//    private final TXMLConnector64 txmlConnector;
//    private final ExecutorService executorService;
////    private final XmlMapper xmlMapper;
//
//    private final Utils utils;
//    private final ObjectMapper objectMapper;
//    private final DataContext dataContext;
//
//    /** Protect callback from GC */
//    private TXMLConnector64.TCallback callbackHolder;
//
//    private final Map<String, Consumer<String>> callbackHandlers;
////    private final Map<String, Function<String, Callback>> callbackHandlers2;
//
//    private final StringPublisher publisher;
//
//
//    public ConnectorWrapper(Utils utils, ObjectMapper objectMapper, DataContext dataContext) throws ConnectorWrapperException, ParserConfigurationException {
//
//        publisher = new StringPublisher();
//
//        txmlConnector = Native.load("txmlconnector64.dll", TXMLConnector64.class);
//        if(txmlConnector == null) {
//            throw new ConnectorWrapperException("TXMLConnector64 object not created");
//        }
//        executorService = Executors.newCachedThreadPool();
//
//        this.utils = utils;
//        this.objectMapper = objectMapper;
//        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
//        this.dataContext = dataContext;
//
////        this.xmlMapper = new XmlMapper();
////        this.xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
////        this.xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
////        this.xmlMapper.registerModule(new JavaTimeModule());
//
//        callbackHandlers = new HashMap<>();
//        callbackHandlers.put("error", this::onErrorCallback);
//        callbackHandlers.put("authentication", this::onAuthenticationCallback);
//        callbackHandlers.put("markets", this::onMarketsCallback);
//        callbackHandlers.put("boards", this::onBoardsCallback);
//        callbackHandlers.put("candlekinds", this::onCandlekindsCallback);
//        callbackHandlers.put("securities", this::onSecuritiesCallback);
//        callbackHandlers.put("pits", this::onPitsCallback);
//        callbackHandlers.put("sec_info_upd", this::onSecurityInfoUpdateCallback);
//        callbackHandlers.put("client", this::onClientCallback);
//        callbackHandlers.put("positions", this::onPositionsCallback);
//        callbackHandlers.put("overnight", this::onOvernightCallback);
//        callbackHandlers.put("messages", this::onMessagesCallback);
//        callbackHandlers.put("server_status", this::onServerStatusCallback);
//        callbackHandlers.put("connector_version", this::onConnectorVersionCallback);
//
////        callbackHandlers2 = new HashMap<>();
////        callbackHandlers2.put("server_status", this::onServerStatusCallback2);
//    }
//
////    @PostConstruct
//    public void init() {
//        Flux
//                .<String>generate(sink -> sink.next("hello"))
//                .take(10)
//                .subscribe(s -> log.info(s.toString()));
//
//        Flux<String> flux = Flux
//                .<String>generate(sink -> sink.next("hello"))
//                .take(10);
//
//        flux.subscribe(log::info);
//
//        ConnectableFlux<String> connectableFlux = flux.publish();
//        connectableFlux.log();
//        connectableFlux.subscribe(log::info);
//        connectableFlux.connect();
//
//        Flux
//                .create(sink -> {
//                    flux.subscribe(new BaseSubscriber<String>() {
//                        @Override
//                        protected void hookOnNext(String value) {
//                            sink.next(value);
//                        }
//
//                        @Override
//                        protected void hookOnComplete() {
//                            sink.complete();
//                        }
//                    });
//
//                    sink.onRequest(r -> {
//                       sink.next("next: " + flux.blockFirst());
//                    });
//                })
//                .subscribe(s -> log.info(s.toString()));
//
//
//        Flux<String> from = Flux.from(publisher);
//
//        from.parallel().subscribe(s -> {
//            log.info("before: " + s);
//            try {
//                Thread.sleep(5_000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//    }
//
//    public void handleCallback(Pointer pData) {
//        executorService.execute(() -> {
//            String xmlData = utils.pointerToString(pData);
//            freeMemory(pData);
//            String rootName = utils.getRootElementName(xmlData);
//            if(callbackHandlers.containsKey(rootName))
//                callbackHandlers.get(rootName).accept(xmlData);
//            else
//                log.info("unhandled callback: " + rootName);
//        });
//    }
//
//    public void onErrorCallback(String data) {
//        Error error = utils.deserializeCallback(data, Error.class);
//        log.error(error.getMessage());
//    }
//
//    public void onAuthenticationCallback(String data) {
//        Authentication authentication = utils.deserializeCallback(data, Authentication.class);
//        dataContext.setAuthentication(authentication);
//    }
//
//    public void onMarketsCallback(String data) {
//        Markets markets = utils.deserializeCallback(data, Markets.class);
//        dataContext.getMarkets().addAll(markets.getItems());
//    }
//
//    public void onBoardsCallback(String data) {
//        Boards boards = utils.deserializeCallback(data, Boards.class);
//        dataContext.getBoards().addAll(boards.getItems());
//    }
//
//    public void onCandlekindsCallback(String data) {
//        Candlekinds candlekinds = utils.deserializeCallback(data, Candlekinds.class);
//        dataContext.getCandleKinds().addAll(candlekinds.getItems());
//    }
//
//    public void onSecuritiesCallback(String data) {
//        Securities securities = utils.deserializeCallback(data, Securities.class);
//        dataContext.getSecurities().addAll(securities.getItems());
//    }
//
//    public void onPitsCallback(String data) {
//        Pits pits = utils.deserializeCallback(data, Pits.class);
//        dataContext.getPits().addAll(pits.getItems());
//    }
//
//    public void onSecurityInfoUpdateCallback(String data) {
//        SecurityInfoUpdate securityInfoUpdate = utils.deserializeCallback(data, SecurityInfoUpdate.class);
//        dataContext.getSecurityInfoUpdates().add(securityInfoUpdate);
//    }
//
//    public void onClientCallback(String data) {
//        Client client = utils.deserializeCallback(data, Client.class);
//        dataContext.getClients().add(client);
//    }
//
//    public void onPositionsCallback(String data) {
//        Positions positions = utils.deserializeCallback(data, Positions.class);
//        dataContext.setPositions(positions);
//    }
//
//    public void onOvernightCallback(String data) {
//        Overnight overnight = utils.deserializeCallback(data, Overnight.class);
//        dataContext.setOvernight(overnight);
//    }
//
//    public void onMessagesCallback(String data) {
//        Messages messages = utils.deserializeCallback(data, Messages.class);
//        dataContext.getMessages().addAll(messages.getItems());
//    }
//
//    public void onServerStatusCallback(String data) {
//        ServerStatus serverStatus = utils.deserializeCallback(data, ServerStatus.class);
//        dataContext.setServerStatus(serverStatus);
//        log.info("server status: " + serverStatus.getConnected());
//    }
//
////    public Callback onServerStatusCallback2(String data) {
////        return utils.deserializeCallback(data, ServerStatus2.class);
////    }
//
//
//    public void onConnectorVersionCallback(String data) {
//        ConnectorVersion connectorVersion = utils.deserializeCallback(data, ConnectorVersion.class);
//        dataContext.setConnectorVersion(connectorVersion);
//    }
//
//    public void setCallback() throws ConnectorWrapperException {
//        TXMLConnector64.TCallback callback = new TXMLConnector64.TCallback() {
//            @Override
//            public boolean callback(Pointer pData) {
//                //create observable
//
////                Flux<String> just = Flux.just("");
////                SubmissionPublisher<String> stringSubmissionPublisher = new SubmissionPublisher<>();
////                Flux<String > from = (Flux<String>) Flux.from(stringSubmissionPublisher);
//
////                String xmlData = utils.pointerToString(pData);
////                freeMemory(pData);
////                log.info(LocalDateTime.now().toString());
////                publisher.publish(xmlData);
////                log.info(LocalDateTime.now().toString());
//
//                handleCallback(pData);
//                return true;
//            }
//        };
//
//        boolean setCallback = txmlConnector.SetCallback(callback);
//        if(!setCallback) {
//            throw new ConnectorWrapperException("Native method SetCallback returned false");
//        }
//
//        callbackHolder = callback;
//    }
//
//    public void initialize() throws ConnectorWrapperException {
//        Path logsPath = Paths.get("logs").toAbsolutePath();
//        try {
//            Files.createDirectories(logsPath);
//        } catch (IOException e) {
//            throw new ConnectorWrapperException("Failed to create logs directory", e);
//        }
//        Pointer pathPtr = utils.stringToPointer(logsPath.toString());
//        Pointer initializePtr = txmlConnector.Initialize(pathPtr, LogLevel.MAXIMUM.value);
//        String initialize = "0";
//
//        try {
//            if(initializePtr != Pointer.NULL) {
//                initialize = utils.pointerToString(initializePtr);
//                throw new ConnectorWrapperException(initialize);
//            }
//        }
//        finally {
//            log.info("Initialize result: {}", initialize);
//            // Native.free(Pointer.nativeValue(initialize));
//            freeMemory(initializePtr);
//        }
//    }
//
//    public void uninitialize() throws ConnectorWrapperException {
//        Pointer unInitializePtr = txmlConnector.UnInitialize();
//        String unInitialize = "0";
//        try {
//            if(unInitializePtr != Pointer.NULL) {
//                unInitialize = utils.pointerToString(unInitializePtr);
//                throw new ConnectorWrapperException(unInitialize);
//            }
//        }
//        finally {
//            log.info("UnInitialize result: {}", unInitialize);
////            Native.free(Pointer.nativeValue(unInitialize));
//            freeMemory(unInitializePtr);
//        }
//    }
//
//    public <T extends Command, V extends Result> V sendCommand(T command, Class<V> clazz) throws ConnectorWrapperException {
//        try {
//            String commandString = utils.serializeCommand(command);
//            Pointer commandPtr = utils.stringToPointer(commandString);
//            log.info("SendCommand: {}", commandString);
//            Pointer responsePtr = txmlConnector.SendCommand(commandPtr);
//            String responseString = utils.pointerToString(responsePtr);
//            freeMemory(responsePtr);
//            log.info("SendCommand response: {}", responseString);
//            String responseRootElement = utils.getRootElementName(responseString);
//            switch (responseRootElement) {
//                case "error":
//                    Error error = utils.deserializeCallback(responseString, Error.class);
//                    throw new SendCommandException("Command " + command.getId() + " error: " + error.getMessage());
//                case "result":
//                    return utils.deserializeCallback(responseString, clazz);
//                default:
//                    throw new UnsupportedOperationException("Unexpected element name: " + responseRootElement);
//            }
//        }
//        catch (Exception e) {
//            throw new ConnectorWrapperException("Failed to send command " + command.getId(), e);
//        }
//    }
//
//    public Result connect() throws ConnectorWrapperException, IOException {
//        ConnectionProfile connectionProfile = utils.getConnectionProfile();
////        ConnectionProfile connectionProfile = utils.getDemoConnectionProfile();
//
//        Connect connect = Connect.builder()
//                .login(connectionProfile.getLogin())
//                .password(connectionProfile.getPassword())
//                .host(connectionProfile.getHost())
//                .port(connectionProfile.getPort())
//                .autoPositions(true)
//                .milliseconds(true)
//                .requestDelay(100)
//                .sessionTimeout(30)
//                .requestTimeout(10)
//                .build();
//
//        return sendCommand(connect, Result.class);
//    }
//
//    public Result getConnectorVersion() throws ConnectorWrapperException {
//        GetConnectorVersion getConnectorVersion = new GetConnectorVersion();
//        return sendCommand(getConnectorVersion, Result.class);
//    }
//
//    public Result disconnect() throws ConnectorWrapperException {
//        Disconnect disconnect = new Disconnect();
//        return sendCommand(disconnect, Result.class);
//    }
//
//    private void freeMemory(Pointer pData) {
//        boolean freeMemory = txmlConnector.FreeMemory(pData);
//        if(!freeMemory) {
//            log.error("error while FreeMemory call");
//        }
//    }
//
//    public static class StringPublisher implements Publisher<String> {
//
//        private Subscriber<String> subscriber;
//
//        @Override
//        public void subscribe(Subscriber<? super String> subscriber) {
//            this.subscriber = (Subscriber<String>) subscriber;
//        }
//
//        public void publish(String str) {
//            subscriber.onNext(str);
//        }
//    }
}
