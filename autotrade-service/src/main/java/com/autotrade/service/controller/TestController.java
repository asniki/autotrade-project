package com.autotrade.service.controller;

import com.autotrade.connector.component.ConnectorWrapperFlux;
import com.autotrade.connector.component.DataContext;
import com.autotrade.connector.exception.ConnectorWrapperException;
import com.autotrade.connector.model.callback.*;
import com.autotrade.connector.component.ConnectorWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class TestController {

//    private Runnable queueHandlerHolder;

    private final ConnectorWrapper connector;

    private final DataContext dataContext;
    private final ObjectMapper objectMapper;

    public TestController(ConnectorWrapper connectorWrapper, DataContext dataContext, ObjectMapper objectMapper) {
        this.connector = connectorWrapper;
        this.dataContext = dataContext;
        this.objectMapper = objectMapper;
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @SneakyThrows
    @PostConstruct
    public void init() {
        try {
            connector.setCallback();
            connector.initialize();
            connector.connect();
            Thread.sleep(10_000);
            connector.getConnectorVersion();
            Thread.sleep(5_000);

            log.info("SecurityInfoUpdates: " + dataContext.getSecurityInfoUpdates().size());
            log.info("SecurityInfoUpdates getPointCost: " + dataContext.getSecurityInfoUpdates().get(0).getPointCost());

            log.info("server status: " + dataContext.getServerStatus().getConnected());

            int clientMarket = dataContext.getClients().get(0).getMarket();
            Map<Integer, List<Boards.Board>> boardsByMarket = dataContext.getBoards().stream().collect(Collectors.groupingBy(Boards.Board::getMarket));
            Map<Integer, List<Pits.Pit>> pitsByMarket = dataContext.getPits().stream().collect(Collectors.groupingBy(Pits.Pit::getMarket));
            Map<Integer, Map<String, List<Pits.Pit>>> pitsByMarketAndBoard = dataContext.getPits().stream().collect(Collectors.groupingBy(Pits.Pit::getMarket, Collectors.groupingBy(Pits.Pit::getBoard)));
            List<Integer> moneyPositionMarkets = dataContext.getPositions().getMoneyPositions().get(0).getMarkets();
            List<Integer> securityPositionMarkets = dataContext.getPositions().getSecurityPositions().stream().map(Positions.SecurityPosition::getMarket).collect(Collectors.toList());
            Map<Integer, List<Securities.Security>> securitiesByMarket = dataContext.getSecurities().stream().collect(Collectors.groupingBy(Securities.Security::getMarket));
            Map<Integer, List<SecurityInfoUpdate>> securityInfoUpdatesByMarket = dataContext.getSecurityInfoUpdates().stream().collect(Collectors.groupingBy(SecurityInfoUpdate::getMarket));

            //TODO выбрать из всех справочников SBER
            List<Securities.Security> sberSecurities = dataContext.getSecurities().stream().filter(s -> s.getSecurityCode().equals("SBER")).collect(Collectors.toList());
            List<SecurityInfoUpdate> sberSecurityInfoUpdates = dataContext.getSecurityInfoUpdates().stream().filter(s -> s.getSecurityCode().equals("SBER")).collect(Collectors.toList());
            List<Pits.Pit> sberPits = dataContext.getPits().stream().filter(s -> s.getSecurityCode().equals("SBER")).collect(Collectors.toList());

            //TODO get_securities_info (3.20) + sec_info (4.7)


            connector.disconnect();
//            Thread.sleep(5_000);
            connector.uninitialize();
        }
        catch (ConnectorWrapperException | InterruptedException e) {
//        catch (ConnectorWrapperException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() throws JsonProcessingException {
        return ResponseEntity.ok(objectMapper.writeValueAsString("123"));
    }
}
