package com.autotrade.connector.model.callback;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Доступные рынки
 * Сообщение приходит:
 * как асинхронный ответ на команду connect,
 * как асинхронный ответ на команду get_markets
 */
@JacksonXmlRootElement(localName = "markets")
@EqualsAndHashCode(callSuper = true)
@Data
public class Markets extends Callback {

    @JacksonXmlProperty(localName = "market")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Market> items;

    public Markets() {
        this.kind = "markets";
    }

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
