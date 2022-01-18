package com.autotrade.connector.component;

import com.autotrade.connector.model.callback.*;
import com.autotrade.connector.model.command.Command;
import com.autotrade.connector.model.response.Error;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// https://stackoverflow.com/questions/6916385/is-there-a-concurrent-list-in-javas-jdk
@Component
@Slf4j
public class DataContext {

    @Getter
    @Setter
    private volatile ServerStatus serverStatus;

    @Getter
    @Setter
    private volatile ConnectorVersion connectorVersion;

    @Getter
    @Setter
    private volatile Positions positions;

    @Getter
    @Setter
    private volatile Authentication authentication;

    @Getter
    @Setter
    private volatile Overnight overnight;

    @Getter
    @Setter
    private List<Securities.Security> securities;

    @Getter
    @Setter
    private List<SecurityInfoUpdate> securityInfoUpdates;

    @Getter
    @Setter
    private List<Pits.Pit> pits;

    @Getter
    @Setter
    private List<Boards.Board> boards;

    @Getter
    @Setter
    private List<Candlekinds.CandleKind> candleKinds;

    @Getter
    @Setter
    private List<Client> clients;

    @Getter
    @Setter
    private List<Markets.Market> markets;

    @Getter
    @Setter
    private List<Messages.Message> messages;



    @Getter
    @Setter
    private volatile ServerStatus2 serverStatus2;

    @Getter
    @Setter
    private volatile ConnectorVersion2 connectorVersion2;

    @Getter
    @Setter
    private volatile Positions2 positions2;

    @Getter
    @Setter
    private volatile Authentication2 authentication2;

    @Getter
    @Setter
    private volatile Overnight2 overnight2;

    @Getter
    @Setter
    private List<Securities2.Security> securities2;

    @Getter
    @Setter
    private List<SecurityInfoUpdate2> securityInfoUpdates2;

    @Getter
    @Setter
    private List<Pits2.Pit> pits2;

    @Getter
    @Setter
    private List<Boards2.Board> boards2;

    @Getter
    @Setter
    private List<Candlekinds2.CandleKind> candleKinds2;

    @Getter
    @Setter
    private List<Client2> clients2;

    @Getter
    @Setter
    private List<Markets2.Market> markets2;

    @Getter
    @Setter
    private List<Messages2.Message> messages2;



    public DataContext() {
        securities = Collections.synchronizedList(new ArrayList<>());
        securityInfoUpdates = Collections.synchronizedList(new ArrayList<>());
        pits = Collections.synchronizedList(new ArrayList<>());
        boards = Collections.synchronizedList(new ArrayList<>());
        candleKinds = Collections.synchronizedList(new ArrayList<>());
        clients = Collections.synchronizedList(new ArrayList<>());
        markets = Collections.synchronizedList(new ArrayList<>());
        messages = Collections.synchronizedList(new ArrayList<>());

        securities2 = Collections.synchronizedList(new ArrayList<>());
        securityInfoUpdates2 = Collections.synchronizedList(new ArrayList<>());
        pits2 = Collections.synchronizedList(new ArrayList<>());
        boards2 = Collections.synchronizedList(new ArrayList<>());
        candleKinds2 = Collections.synchronizedList(new ArrayList<>());
        clients2 = Collections.synchronizedList(new ArrayList<>());
        markets2 = Collections.synchronizedList(new ArrayList<>());
        messages2 = Collections.synchronizedList(new ArrayList<>());
    }

    public void reset() {
        serverStatus = null;
        connectorVersion = null;
        positions = null;
        authentication = null;
        overnight = null;

        securities.clear();
        securityInfoUpdates.clear();
        pits.clear();
        boards.clear();
        candleKinds.clear();
        clients.clear();
        markets.clear();
        messages.clear();
    }

    public <T> void onErrorCallback(T data) {
        Error error = (Error) data;
        log.error(error.getMessage());
    }

    public <T> void onAuthenticationCallback(T data) {
        Authentication2 authentication = (Authentication2) data;
        setAuthentication2(authentication);
    }

    public <T> void onMarketsCallback(T data) {
        Markets2 markets = (Markets2) data;
        getMarkets2().addAll(markets.getItems());
    }

    public <T> void onBoardsCallback(T data) {
        Boards2 boards = (Boards2) data;
        getBoards2().addAll(boards.getItems());
    }

    public <T> void onCandlekindsCallback(T data) {
        Candlekinds2 candlekinds = (Candlekinds2) data;
        getCandleKinds2().addAll(candlekinds.getItems());
    }

    public <T> void onSecuritiesCallback(T data) {
        Securities2 securities = (Securities2) data;
        getSecurities2().addAll(securities.getItems());
    }

    public <T> void onPitsCallback(T data) {
        Pits2 pits = (Pits2) data;
        getPits2().addAll(pits.getItems());
    }

    public <T> void onSecurityInfoUpdateCallback(T data) {
        SecurityInfoUpdate2 securityInfoUpdate = (SecurityInfoUpdate2) data;
        getSecurityInfoUpdates2().add(securityInfoUpdate);
    }

    public <T> void onClientCallback(T data) {
        Client2 client = (Client2) data;
        getClients2().add(client);
    }

    public <T> void onPositionsCallback(T data) {
        Positions2 positions = (Positions2) data;
        setPositions2(positions);
    }

    public <T> void onOvernightCallback(T data) {
        Overnight2 overnight = (Overnight2) data;
        setOvernight2(overnight);
    }

    public <T> void onMessagesCallback(T data) {
        Messages2 messages = (Messages2) data;
        getMessages2().addAll(messages.getItems());
    }

    public <T> void onServerStatusCallback(T data) {
        ServerStatus2 serverStatus = (ServerStatus2) data;
        setServerStatus2(serverStatus);
        log.info("server status: " + serverStatus.getConnected());
    }

    public <T> void onConnectorVersionCallback(T data) {
        ConnectorVersion2 connectorVersion = (ConnectorVersion2) data;
        setConnectorVersion2(connectorVersion);
    }
}
