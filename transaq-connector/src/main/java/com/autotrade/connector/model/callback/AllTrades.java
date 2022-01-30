package com.autotrade.connector.model.callback;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

/**
 * Сделки по инструментам.
 * Сообщение приходит:
 * как асинхронный ответ после команды subscribe.
 */
@JacksonXmlRootElement(localName = "alltrades")
@EqualsAndHashCode(callSuper = true)
@Data
public class AllTrades extends Callback {
    @JacksonXmlProperty(localName = "trade")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Trade> items;

    public AllTrades() {
        this.kind = "alltrades";
    }

    /**
     * Сделка
     */
    @Data
    public static class Trade {
        /** Идентификатор инструмента */
        @JacksonXmlProperty(isAttribute = true, localName = "secid")
        private int securityId;

        /** Код инструмента */
        @JacksonXmlProperty(localName = "seccode")
        private String securityCode;

        /** Биржевой номер сделки */
        @JacksonXmlProperty(localName = "tradeno")
        private long tradeNo;

        /** Время сделки */
        @JacksonXmlProperty(localName = "time")
        private String time;                    //TODO time

        /** Идентификатор режима торгов */
        @JacksonXmlProperty(localName = "board")
        private String board;

        /** Цена сделки */
        @JacksonXmlProperty(localName = "price")
        private String priceHolder;

        /** Цена сделки */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal price = (priceHolder == null) ? null : new BigDecimal(priceHolder);

        /** Объем сделки */
        @JacksonXmlProperty(localName = "quantity")
        private int quantity;

        /** Покупка (B) / продажа (S) */
        @JacksonXmlProperty(localName = "buysell")
        private String buySell;

        /** Количество открытых позиций на срочном рынке */
        @JacksonXmlProperty(localName = "openinterest")
        private Integer openInterest;

        /** Период торгов (O - открытие, N - торги, С - закрытие) */
        @JacksonXmlProperty(localName = "period")
        private String period;
    }
}
