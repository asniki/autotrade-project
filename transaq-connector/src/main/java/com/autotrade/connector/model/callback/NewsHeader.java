package com.autotrade.connector.model.callback;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * Заголовок новости.
 * Сообщение приходит:
 * как асинхронный ответ на команду get_old_news
 */
@JacksonXmlRootElement(localName = "news_header")
@EqualsAndHashCode(callSuper = true)
@Data
public class NewsHeader extends Callback {
    /** Порядковый номер новости */
    @JacksonXmlProperty(localName = "id")
    private int id;

    /** Дата-время новости (от источника) */
    @JacksonXmlProperty(localName = "timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime timestamp;

    /** Источник новости */
    @JacksonXmlProperty(localName = "source")
    private String source;

    /** Заголовок новости */
    @JacksonXmlProperty(localName = "title")
    private String title;

    public NewsHeader() {
        this.kind = "news_header";
    }
}

