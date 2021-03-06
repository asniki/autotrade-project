package com.autotrade.connector.model.callback;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.Data;

import java.util.List;

/**
 * Доступные рынки
 */
@JacksonXmlRootElement(localName = "markets")
@Data
public class Markets {

    @JacksonXmlProperty(localName = "market")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Market> items;

    /**
     * Рынок
     */
    @Data
    public static class Market {
        /** Внутренний код рынка */
        @JacksonXmlProperty(isAttribute = true, localName = "id")
        private int id;

        /** Название рынка */
        @JacksonXmlText
        private String name;
    }
}
