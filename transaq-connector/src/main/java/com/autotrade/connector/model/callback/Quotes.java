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
 * Глубина рынка по инструментам (стакан).
 * Сообщение приходит:
 * как асинхронный ответ после команды subscribe.
 */
@JacksonXmlRootElement(localName = "quotes")
@EqualsAndHashCode(callSuper = true)
@Data
public class Quotes extends Callback {
    @JacksonXmlProperty(localName = "quote")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Quote> items;

    public Quotes() {
        this.kind = "quotes";
    }

    /**
     * Сделка
     */
    @Data
    public static class Quote {
        /** Идентификатор инструмента */
        @JacksonXmlProperty(isAttribute = true, localName = "secid")
        private int securityId;

        /** Идентификатор режима торгов */
        @JacksonXmlProperty(localName = "board")
        private String board;

        /** Код инструмента */
        @JacksonXmlProperty(localName = "seccode")
        private String securityCode;

        /** Цена */
        @JacksonXmlProperty(localName = "price")
        private String priceHolder;

        /** Цена */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal price = (priceHolder == null) ? null : new BigDecimal(priceHolder);

        /** Источник котировки (маркетмейкер) */
        @JacksonXmlProperty(localName = "source")
        private String source;

        /** Доходность (актуально только для облигаций) */
        @JacksonXmlProperty(localName = "yield")
        private Integer yield;

        /** Количество бумаг к покупке */
        @JacksonXmlProperty(localName = "buy")
        private int buy;

        /** Количество бумаг к продаже */
        @JacksonXmlProperty(localName = "sell")
        private int sell;
    }
}
