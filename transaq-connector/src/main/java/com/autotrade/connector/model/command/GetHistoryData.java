package com.autotrade.connector.model.command;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Выдать последние N свечей заданного периода, по заданному инструменту.
 * Результатом является структура candles.
 */
@JacksonXmlRootElement(localName = "command")
@Builder
@EqualsAndHashCode(callSuper = true)
@Data
public class GetHistoryData extends Command {
    /** Инструмент */
    @JacksonXmlProperty(localName = "security")
    private Security security;

    /** Идентификатор периода */
    @JacksonXmlProperty(localName = "period")
    private int period;

    /** Количество свечей */
    @JacksonXmlProperty(localName = "count")
    private int count;

    /**
     * true - нужно выдавать самые свежие данные,
     * false - выдавать данные в продолжение предыдущего запроса
     */
    @JacksonXmlProperty(localName = "reset")
    private boolean reset;

    public GetHistoryData() {
        id = "gethistorydata";
    }

    public GetHistoryData(Security security, int period, int count, boolean reset) {
        this();
        this.security = security;
        this.period = period;
        this.count = count;
        this.reset = reset;
    }

    public GetHistoryData(String board, String securityCode, int period, int count, boolean reset) {
        this(new Security(board, securityCode), period, count, reset);
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