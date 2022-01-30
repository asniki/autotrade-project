package com.autotrade.connector.component;

import com.autotrade.connector.model.callback.*;
import com.autotrade.connector.model.response.Error;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

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
    private volatile CurrentServer currentServer;

    @Getter
    @Setter
    private List<NewsHeader> newsHeaders;

    @Getter
    @Setter
    private List<NewsBody> newsBodies;

    /** Map< securityCode, Map< period, List< Candles.Candle >>> */
    @Getter
    @Setter
    private Map<String, Map<Integer, List<Candles.Candle>>> securityHistory;

    @Getter
    @Setter
    private List<SecurityInfo> securityInfos;

    @Getter
    @Setter
    private List<Quotations.Quotation> quotations;

    @Getter
    @Setter
    private List<AllTrades.Trade> trades;

    @Getter
    @Setter
    private List<List<Quotes.Quote>> quotesLists;


    public DataContext() {
        securities = Collections.synchronizedList(new ArrayList<>());
        securityInfoUpdates = Collections.synchronizedList(new ArrayList<>());
        pits = Collections.synchronizedList(new ArrayList<>());
        boards = Collections.synchronizedList(new ArrayList<>());
        candleKinds = Collections.synchronizedList(new ArrayList<>());
        clients = Collections.synchronizedList(new ArrayList<>());
        markets = Collections.synchronizedList(new ArrayList<>());
        messages = Collections.synchronizedList(new ArrayList<>());
        newsHeaders = Collections.synchronizedList(new ArrayList<>());
        newsBodies = Collections.synchronizedList(new ArrayList<>());
        securityHistory = Collections.synchronizedMap(new HashMap<>());
        securityInfos = Collections.synchronizedList(new ArrayList<>());
        quotations = Collections.synchronizedList(new ArrayList<>());
        trades = Collections.synchronizedList(new ArrayList<>());
        quotesLists = Collections.synchronizedList(new ArrayList<>());
    }

    public void reset() {
        serverStatus = null;
        connectorVersion = null;
        positions = null;
        authentication = null;
        overnight = null;
        currentServer = null;

        securities.clear();
        securityInfoUpdates.clear();
        pits.clear();
        boards.clear();
        candleKinds.clear();
        clients.clear();
        markets.clear();
        messages.clear();
        newsHeaders.clear();
        newsBodies.clear();
        securityHistory.clear();
        securityInfos.clear();
        quotations.clear();
        trades.clear();
        quotesLists.clear();
    }

    public <T> void onErrorCallback(T data) {
        Error error = (Error) data;
        log.error("[onErrorCallback] " + error.getMessage());
    }

    public <T> void onAuthenticationCallback(T data) {
        Authentication newAuthentication = (Authentication) data;
        setAuthentication(newAuthentication);
    }

    public <T> void onMarketsCallback(T data) {
        Markets newMarkets = (Markets) data;
        if(serverStatus != null && serverStatus.getConnected().equals("true") && newMarkets.getItems().size() > 0) {
            log.info("[onMarketsCallback] Markets previous count: " + getMarkets().size());
            log.info("[onMarketsCallback] Markets new count: " + newMarkets.getItems().size());
            markets.clear();
        }
        getMarkets().addAll(newMarkets.getItems());
    }

    public <T> void onBoardsCallback(T data) {
        Boards newBoards = (Boards) data;
        getBoards().addAll(newBoards.getItems());
    }

    public <T> void onCandlekindsCallback(T data) {
        Candlekinds newCandlekinds = (Candlekinds) data;
        getCandleKinds().addAll(newCandlekinds.getItems());
    }

    public <T> void onSecuritiesCallback(T data) {
        Securities newSecurities = (Securities) data;
        if(serverStatus != null && serverStatus.getConnected().equals("true") && newSecurities.getItems().size() > 0) {
            log.info("[onSecuritiesCallback] Securities previous count: " + getSecurities().size());
            log.info("[onSecuritiesCallback] Securities new count: " + newSecurities.getItems().size());
            securities.clear();
        }
        getSecurities().addAll(newSecurities.getItems());
    }

    public <T> void onPitsCallback(T data) {
        Pits newPits = (Pits) data;
        getPits().addAll(newPits.getItems());
    }

    public <T> void onSecurityInfoUpdateCallback(T data) {
        SecurityInfoUpdate newSecurityInfoUpdate = (SecurityInfoUpdate) data;
        getSecurityInfoUpdates().add(newSecurityInfoUpdate);
    }

    public <T> void onClientCallback(T data) {
        Client newClient = (Client) data;
        getClients().add(newClient);
    }

    public <T> void onPositionsCallback(T data) {
        Positions newPositions = (Positions) data;
        setPositions(newPositions);
    }

    public <T> void onOvernightCallback(T data) {
        Overnight newOvernight = (Overnight) data;
        setOvernight(newOvernight);
    }

    public <T> void onMessagesCallback(T data) {
        Messages newMessages = (Messages) data;
        getMessages().addAll(newMessages.getItems());
    }

    public <T> void onServerStatusCallback(T data) {
        ServerStatus newServerStatus = (ServerStatus) data;
        setServerStatus(newServerStatus);
        log.info("[onServerStatusCallback] server status: " + newServerStatus.getConnected());
    }

    public <T> void onConnectorVersionCallback(T data) {
        ConnectorVersion newConnectorVersion = (ConnectorVersion) data;
        setConnectorVersion(newConnectorVersion);
    }

    public <T> void onCurrentServerCallback(T data) {
        CurrentServer newCurrentServer = (CurrentServer) data;
        setCurrentServer(newCurrentServer);
    }

    public <T> void onNewsHeaderCallback(T data) {
        NewsHeader newNewsHeader = (NewsHeader) data;
        getNewsHeaders().add(newNewsHeader);
    }

    public <T> void onNewsBodyCallback(T data) {
        NewsBody newNewsBody = (NewsBody) data;
        getNewsBodies().add(newNewsBody);
    }

    public <T> void onCandlesCallback(T data) {
        Candles newCandles = (Candles) data;
        String securityCode = newCandles.getSecurityCode();
        int period = newCandles.getPeriod();

        log.info("[onCandlesCallback] get " + newCandles.getItems().size() + " candles for " + securityCode);

        Map<String, Map<Integer, List<Candles.Candle>>> securityHistory = getSecurityHistory();
        if(!securityHistory.containsKey(newCandles.getSecurityCode())) {
            securityHistory.put(securityCode, new HashMap<>());
        }
        Map<Integer, List<Candles.Candle>> historyByPeriod = securityHistory.get(securityCode);

        if(!historyByPeriod.containsKey(period)) {
            historyByPeriod.put(period, new ArrayList<>());
        }

        historyByPeriod.get(period).addAll(newCandles.getItems());
    }

    public <T> void onSecurityInfo(T data) {
        SecurityInfo newSecurityInfo = (SecurityInfo) data;
        getSecurityInfos().add(newSecurityInfo);

    }

    public <T> void onQuotations(T data) {
        Quotations newQuotations = (Quotations) data;
        getQuotations().addAll(newQuotations.getItems());
    }

    public <T> void onAllTrades(T data) {
        AllTrades newAllTrades = (AllTrades) data;
        trades.addAll(newAllTrades.getItems());
    }

    public <T> void onQuotes(T data) {
        Quotes newQuotes = (Quotes) data;
        getQuotesLists().add(newQuotes.getItems());
    }
}
