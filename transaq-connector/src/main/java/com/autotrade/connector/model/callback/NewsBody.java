package com.autotrade.connector.model.callback;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Тело новости.
 * Сообщение приходит:
 * как асинхронный ответ на команду get_news_body
 */
@JacksonXmlRootElement(localName = "news_body")
@EqualsAndHashCode(callSuper = true)
@Data
public class NewsBody extends Callback {
    /** Порядковый номер новости */
    @JacksonXmlProperty(localName = "id")
    private int id;

    /** Текст новости */
    @JacksonXmlProperty(localName = "text")
    private String text;

    public NewsBody() {
        this.kind = "news_body";
    }
}

