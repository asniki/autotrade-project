package com.autotrade.connector.model.callback;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;

/**
 * Котировки по инструментам.
 * Сообщение приходит:
 * как асинхронный ответ после команды subscribe.
 */
@JacksonXmlRootElement(localName = "quotations")
@EqualsAndHashCode(callSuper = true)
@Data
public class Quotations extends Callback {
    @JacksonXmlProperty(localName = "quotation")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Quotation> items;

    public Quotations() {
        this.kind = "quotations";
    }

    /**
     * Котировка
     */
    @Data
    public static class Quotation {
        /** Идентификатор инструмента */
        @JacksonXmlProperty(isAttribute = true, localName = "secid")
        private int securityId;

        /** Идентификатор режима торгов */
        @JacksonXmlProperty(localName = "board")
        private String board;

        /** Код инструмента */
        @JacksonXmlProperty(localName = "seccode")
        private String securityCode;

        /** НКД (Накопленный купонный доход) на дату торгов в расчете на одну бумагу, руб */
        @JacksonXmlProperty(localName = "accruedintvalue")
        private String accruedInterestHolder;

        /** НКД (Накопленный купонный доход) на дату торгов в расчете на одну бумагу, руб */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal accruedInterest = (accruedInterestHolder == null) ? null : new BigDecimal(accruedInterestHolder);

        /** Цена первой сделки */
        @JacksonXmlProperty(localName = "open")
        private String openHolder;

        /** Цена первой сделки */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal open = (openHolder == null) ? null : new BigDecimal(openHolder);

        /** Средневзвешенная цена */
        @JacksonXmlProperty(localName = "waprice")
        private String avgWeightedPriceHolder;

        /** Средневзвешенная цена */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal avgWeightedPrice = (avgWeightedPriceHolder == null) ? null : new BigDecimal(avgWeightedPriceHolder);

        /** Количество лотов на покупку по лучшей цене */
        @JacksonXmlProperty(localName = "biddepth")
        private int bidDepth;

        /** Совокупный спрос */
        @JacksonXmlProperty(localName = "biddeptht")
        private int bidDepthTotal;

        /** Заявок на покупку */
        @JacksonXmlProperty(localName = "numbids")
        private int numberOfBids;

        /** Количество лотов на продажу по лучшей цене */
        @JacksonXmlProperty(localName = "offerdepth")
        private int offerDepth;

        /** Совокупное предложение */
        @JacksonXmlProperty(localName = "offerdeptht")
        private int offerDepthTotal;

        /** Лучшая котировка на покупку */
        @JacksonXmlProperty(localName = "bid")
        private String bidHolder;

        /** Лучшая котировка на покупку */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal bid = (bidHolder == null) ? null : new BigDecimal(bidHolder);

        /** Лучшая котировка на продажу */
        @JacksonXmlProperty(localName = "offer")
        private String offerHolder;

        /** Лучшая котировка на продажу */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal offer = (offerHolder == null) ? null : new BigDecimal(offerHolder);

        /** Заявок на продажу */
        @JacksonXmlProperty(localName = "numoffers")
        private int numberOfOffers;

        /** Сделок */
        @JacksonXmlProperty(localName = "numtrades")
        private int numberOfTrades;

        /** Объем совершенных сделок в лотах */
        @JacksonXmlProperty(localName = "voltoday")
        private int volumeToday;

        /** Общее количество открытых позиций (FORTS) */
        @JacksonXmlProperty(localName = "openpositions")
        private Integer openPositions;

        /** Изменение открытых позиций (FORTS) */
        @JacksonXmlProperty(localName = "deltapositions")
        private Integer deltaPositions;

        /** Цена последней сделки */
        @JacksonXmlProperty(localName = "last")
        private String lastHolder;

        /** Цена последней сделки */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal last = (lastHolder == null) ? null : new BigDecimal(lastHolder);

        /** Объем последней сделки, в лотах */
        @JacksonXmlProperty(localName = "quantity")
        private int quantity;

        /** Время заключения последней сделки */
        @JacksonXmlProperty(localName = "time")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss.SSS")
        private LocalTime time;

        /** Изменение цены последней сделки по отношению к цене последней сделки предыдущего торгового дня */
        @JacksonXmlProperty(localName = "change")
        private String changeHolder;

        /** Изменение цены последней сделки по отношению к цене последней сделки предыдущего торгового дня */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal change = (changeHolder == null) ? null : new BigDecimal(changeHolder);

        /** Цена последней сделки к оценке предыдущего дня */
        @JacksonXmlProperty(localName = "priceminusprevwaprice")
        private String priceMinusPrevAvgWeightedPriceHolder;

        /** Цена последней сделки к оценке предыдущего дня */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal priceMinusPrevAvgWeightedPrice = (priceMinusPrevAvgWeightedPriceHolder == null) ? null : new BigDecimal(priceMinusPrevAvgWeightedPriceHolder);

        /** Объем совершенных сделок, млн. руб */
        @JacksonXmlProperty(localName = "valtoday")
        private String valueTodayHolder;

        /** Объем совершенных сделок, млн. руб */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal valueToday = (valueTodayHolder == null) ? null : new BigDecimal(valueTodayHolder);

        /** Доходность, по цене последней сделки */
        @JacksonXmlProperty(localName = "yield")
        private String yieldHolder;

        /** Доходность, по цене последней сделки */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal yield = (yieldHolder == null) ? null : new BigDecimal(yieldHolder);

        /** Доходность по средневзвешенной цене */
        @JacksonXmlProperty(localName = "yieldatwaprice")
        private String yieldAtAvgWeightedPriceHolder;

        /** Доходность по средневзвешенной цене */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal yieldAtAvgWeightedPrice = (yieldAtAvgWeightedPriceHolder == null) ? null : new BigDecimal(yieldAtAvgWeightedPriceHolder);

        /** Рыночная цена по результатам торгов сегодняшнего дня */
        @JacksonXmlProperty(localName = "marketpricetoday")
        private String marketPriceTodayHolder;

        /** Рыночная цена по результатам торгов сегодняшнего дня */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal marketPriceToday = (marketPriceTodayHolder == null) ? null : new BigDecimal(marketPriceTodayHolder);

        /** Наибольшая цена спроса в течение торговой сессии */
        @JacksonXmlProperty(localName = "highbid")
        private String highBidHolder;

        /** Наибольшая цена спроса в течение торговой сессии */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal highBid = (highBidHolder == null) ? null : new BigDecimal(highBidHolder);

        /** Наименьшая цена предложения в течение торговой сессии */
        @JacksonXmlProperty(localName = "lowoffer")
        private String lowOfferHolder;

        /** Наименьшая цена предложения в течение торговой сессии */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal lowOffer = (lowOfferHolder == null) ? null : new BigDecimal(lowOfferHolder);

        /** Максимальная цена сделки */
        @JacksonXmlProperty(localName = "high")
        private String highHolder;

        /** Максимальная цена сделки */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal high = (highHolder == null) ? null : new BigDecimal(highHolder);

        /** Минимальная цена сделки */
        @JacksonXmlProperty(localName = "low")
        private String lowHolder;

        /** Минимальная цена сделки */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal low = (lowHolder == null) ? null : new BigDecimal(lowHolder);

        /** Цена закрытия */
        @JacksonXmlProperty(localName = "closeprice")
        private String closePriceHolder;

        /** Цена закрытия */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal closePrice = (closePriceHolder == null) ? null : new BigDecimal(closePriceHolder);

        /** Доходность по цене закрытия */
        @JacksonXmlProperty(localName = "closeyield")
        private String closeYieldHolder;

        /** Доходность по цене закрытия */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal closeYield = (closeYieldHolder == null) ? null : new BigDecimal(closeYieldHolder);

        /** Статус «торговые операции разрешены / запрещены» */
        @JacksonXmlProperty(localName = "status")
        private String status;

        /** Состояние торговой сессии по инструменту */
        @JacksonXmlProperty(localName = "tradingstatus")
        private String tradingStatus;

        /** ГО (гарантийное обеспечение) покупок / покрытия */
        @JacksonXmlProperty(localName = "buydeposit")
        private String buyDepositHolder;

        /** ГО (гарантийное обеспечение) покупок / покрытия */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal buyDeposit = (buyDepositHolder == null) ? null : new BigDecimal(buyDepositHolder);

        /** ГО (гарантийное обеспечение) продаж / непокрытия */
        @JacksonXmlProperty(localName = "selldeposit")
        private String sellDepositHolder;

        /** ГО (гарантийное обеспечение) продаж / непокрытия */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal sellDeposit = (sellDepositHolder == null) ? null : new BigDecimal(sellDepositHolder);

        /** Волатильность */
        @JacksonXmlProperty(localName = "volatility")
        private String volatilityHolder;

        /** Волатильность */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal volatility = (volatilityHolder == null) ? null : new BigDecimal(volatilityHolder);

        /** Теоретическая цена */
        @JacksonXmlProperty(localName = "theoreticalprice")
        private String theoreticalPriceHolder;

        /** Теоретическая цена */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal theoreticalPrice = (theoreticalPriceHolder == null) ? null : new BigDecimal(theoreticalPriceHolder);

        /** Базовое ГО (гарантийное обеспечение) под покупку маржируемого опциона */
        @JacksonXmlProperty(localName = "bgo_buy")
        private String bgoBuyHolder;

        /** Базовое ГО (гарантийное обеспечение) под покупку маржируемого опциона */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal bgoBuy = (bgoBuyHolder == null) ? null : new BigDecimal(bgoBuyHolder);

        /** Стоимость пункта цены */
        @JacksonXmlProperty(localName = "point_cost")
        private String pointCostHolder;

        /** Стоимость пункта цены */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal pointCost = (pointCostHolder == null) ? null : new BigDecimal(pointCostHolder);

        /** Официальная текущая цена Биржи */
        @JacksonXmlProperty(localName = "lcurrentprice")
        private String lcurrentPriceHolder;                   //TODO naming

        /** Официальная текущая цена Биржи */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal lcurrentPrice = (lcurrentPriceHolder == null) ? null : new BigDecimal(lcurrentPriceHolder);
    }
}
