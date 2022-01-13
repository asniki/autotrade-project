package com.autotrade.connector.component;

import com.autotrade.connector.model.callback.*;
import com.autotrade.connector.model.command.Command;
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

    public DataContext() {
        securities = Collections.synchronizedList(new ArrayList<>());
        securityInfoUpdates = Collections.synchronizedList(new ArrayList<>());
        pits = Collections.synchronizedList(new ArrayList<>());
        boards = Collections.synchronizedList(new ArrayList<>());
        candleKinds = Collections.synchronizedList(new ArrayList<>());
        clients = Collections.synchronizedList(new ArrayList<>());
        markets = Collections.synchronizedList(new ArrayList<>());
        messages = Collections.synchronizedList(new ArrayList<>());
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

    public <T> void onServerStatusCallback(T data) {
        ServerStatus serverStatus = (ServerStatus) data;
        setServerStatus(serverStatus);
        log.info("server status: " + serverStatus.getConnected());
    }
}
