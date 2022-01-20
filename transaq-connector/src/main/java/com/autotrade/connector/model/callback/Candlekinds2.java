package com.autotrade.connector.model.callback;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Информация о доступных периодах свечей
 * Сообщение приходит:
 * как асинхронный ответ на команду connect,
 */
@JacksonXmlRootElement(localName = "candlekinds")
@EqualsAndHashCode(callSuper = true)
@Data
public class Candlekinds2 extends Callback {

    @JacksonXmlProperty(localName = "kind")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<CandleKind> items;

    public Candlekinds2() {
        this.kind = "candlekinds";
    }

    /**
     * Доступный период свечей
     */
    @Data
    public static class CandleKind {
        /** Идентификатор периода */
        @JacksonXmlProperty(localName = "id")
        private int id;

        /** Количество секунд в периоде */
        @JacksonXmlProperty(localName = "period")
        private int period;

        /** Наименование периода */
        @JacksonXmlProperty(localName = "name")
        private String name;
    }
}
