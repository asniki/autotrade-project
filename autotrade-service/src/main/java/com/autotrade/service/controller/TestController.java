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

    private final ConnectorWrapperFlux connectorFlux;
    private final DataContext dataContext;
    private final ObjectMapper objectMapper;

    public TestController(ConnectorWrapperFlux connectorWrapperFlux, DataContext dataContext, ObjectMapper objectMapper) {
        this.connectorFlux = connectorWrapperFlux;
        this.dataContext = dataContext;
        this.objectMapper = objectMapper;
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @SneakyThrows
    @PostConstruct
    public void init() {
        try {
            connectorFlux.setCallback();
            connectorFlux.initialize();
            connectorFlux.connect();
            Thread.sleep(10_000);
            connectorFlux.getConnectorVersion();
            Thread.sleep(5_000);

            log.info("SecurityInfoUpdates: " + dataContext.getSecurityInfoUpdates2().size());
            log.info("SecurityInfoUpdates getPointCost: " + dataContext.getSecurityInfoUpdates2().get(0).getPointCost());

            log.info("server status: " + dataContext.getServerStatus2().getConnected());

            int clientMarket = dataContext.getClients2().get(0).getMarket();
            Map<Integer, List<Boards2.Board>> boardsByMarket = dataContext.getBoards2().stream().collect(Collectors.groupingBy(Boards2.Board::getMarket));
            Map<Integer, List<Pits2.Pit>> pitsByMarket = dataContext.getPits2().stream().collect(Collectors.groupingBy(Pits2.Pit::getMarket));
            Map<Integer, Map<String, List<Pits2.Pit>>> pitsByMarketAndBoard = dataContext.getPits2().stream().collect(Collectors.groupingBy(Pits2.Pit::getMarket, Collectors.groupingBy(Pits2.Pit::getBoard)));
            List<Integer> moneyPositionMarkets = dataContext.getPositions2().getMoneyPositions().get(0).getMarkets();
            List<Integer> securityPositionMarkets = dataContext.getPositions2().getSecurityPositions().stream().map(Positions2.SecurityPosition::getMarket).collect(Collectors.toList());
            Map<Integer, List<Securities2.Security>> securitiesByMarket = dataContext.getSecurities2().stream().collect(Collectors.groupingBy(Securities2.Security::getMarket));
            Map<Integer, List<SecurityInfoUpdate2>> securityInfoUpdatesByMarket = dataContext.getSecurityInfoUpdates2().stream().collect(Collectors.groupingBy(SecurityInfoUpdate2::getMarket));

            //TODO выбрать из всех справочников SBER
            List<Securities2.Security> sberSecurities = dataContext.getSecurities2().stream().filter(s -> s.getSecurityCode().equals("SBER")).collect(Collectors.toList());
            List<SecurityInfoUpdate2> sberSecurityInfoUpdates = dataContext.getSecurityInfoUpdates2().stream().filter(s -> s.getSecurityCode().equals("SBER")).collect(Collectors.toList());
            List<Pits2.Pit> sberPits = dataContext.getPits2().stream().filter(s -> s.getSecurityCode().equals("SBER")).collect(Collectors.toList());

            //TODO get_securities_info (3.20) + sec_info (4.7)


            connectorFlux.disconnect();
//            Thread.sleep(5_000);
            connectorFlux.uninitialize();
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
