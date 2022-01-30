package com.autotrade.connector.model.command;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Подписка на получение котировок, сделок и глубины рынка (стакана).
 * Результатом является получение сообщений quotations, alltrades, quotes.
 */
@JacksonXmlRootElement(localName = "command")
@Builder
@EqualsAndHashCode(callSuper = true)
@Data
public class Subscribe extends Command {

    /** Подписка на сделки рынка */
    @JacksonXmlProperty(localName = "alltrades")
    private AllTradesSubscription allTrades;

    /** Подписка на изменения показателей торгов */
    @JacksonXmlProperty(localName = "quotations")
    private QuotationsSubscription quotations;

    /** Подписка на изменения стакана */
    @JacksonXmlProperty(localName = "quotes")
    private QuotesSubscription quotes;

    public Subscribe() {
        id = "subscribe";
    }

    public Subscribe(AllTradesSubscription allTrades, QuotationsSubscription quotations, QuotesSubscription quotes) {
        this();
        this.allTrades = allTrades;
        this.quotations = quotations;
        this.quotes = quotes;
    }

    public Subscribe(List<Security> allTrades, List<Security> quotations, List<Security> quotes) {
        this(new AllTradesSubscription(allTrades), new QuotationsSubscription(quotations), new QuotesSubscription(quotes));
    }

    public Subscribe(String board, String securityCode) {
        this();
        Security security = new Security(board, securityCode);
        this.allTrades = new AllTradesSubscription(List.of(security));
        this.quotations = new QuotationsSubscription(List.of(security));
        this.quotes = new QuotesSubscription(List.of(security));
    }

    @Data
    @AllArgsConstructor
    public static class AllTradesSubscription {
        @JacksonXmlProperty(localName = "security")
        @JacksonXmlElementWrapper(useWrapping = false)
        private List<Security> items;
    }

    @Data
    @AllArgsConstructor
    public static class QuotationsSubscription {
        @JacksonXmlProperty(localName = "security")
        @JacksonXmlElementWrapper(useWrapping = false)
        private List<Security> items;
    }

    @Data
    @AllArgsConstructor
    public static class QuotesSubscription {
        @JacksonXmlProperty(localName = "security")
        @JacksonXmlElementWrapper(useWrapping = false)
        private List<Security> items;
    }

    @Data
    @AllArgsConstructor
    public static class Security {
        /** Идентификатор режима торгов */
        @JacksonXmlProperty(localName = "board")
        private String board;

        /** Код инструмента */
        @JacksonXmlProperty(localName = "seccode")
        private String securityCode;
    }
}


