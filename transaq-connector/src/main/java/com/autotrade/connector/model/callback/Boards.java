package com.autotrade.connector.model.callback;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.util.List;

/**
 * Справочник режимов торгов
 */
@JacksonXmlRootElement(localName = "boards")
@Data
public class Boards {

    @JacksonXmlProperty(localName = "board")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Board> items;

    /**
     * Режим торгов
     */
    @Data
    public static class Board {
        /** Идентификатор режима торгов */
        @JacksonXmlProperty(isAttribute = true, localName = "id")
        private String id;

        /** Наименование режима торгов */
        @JacksonXmlProperty(localName = "name")
        private String name;

        /** Внутренний код рынка */
        @JacksonXmlProperty(localName = "market")
        private int market;

        /** Тип режима торгов: 0 = FORTS, 1 = Т+, 2 = Т0 */
        @JacksonXmlProperty(localName = "type")
        private int type;
    }
}
