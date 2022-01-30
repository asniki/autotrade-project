package com.autotrade.connector.model.command;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Запрос на получение информации по инструменту.
 * Результатом является структура sec_info.
 */
@JacksonXmlRootElement(localName = "command")
@Builder
@EqualsAndHashCode(callSuper = true)
@Data
public class GetSecuritiesInfo extends Command {
    /** Инструмент */
    @JacksonXmlProperty(localName = "security")
    private Security security;

    public GetSecuritiesInfo() {
        id = "get_securities_info";
    }

    public GetSecuritiesInfo(Security security) {
        this();
        this.security = security;
    }

    public GetSecuritiesInfo(int market, String securityCode) {
        this(new Security(market, securityCode));
    }

    @Data
    @AllArgsConstructor
    public static class Security {
        /** Внутренний код рынка */
        @JacksonXmlProperty(localName = "market")
        private int market;

        /** Код инструмента */
        @JacksonXmlProperty(localName = "seccode")
        private String securityCode;
    }
}


