package com.autotrade.connector.model.command;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Запрос на получение тела новости для известного заголовка.
 * Результатом является структура news_body.
 */
@JacksonXmlRootElement(localName = "command")
@Builder
@EqualsAndHashCode(callSuper = true)
@Data
public class GetNewsBody extends Command {

    /** Номер новости из полученного ранее заголовка, для которой необходимо получить тело */
    @JacksonXmlProperty(isAttribute = true, localName = "news_id")
    private int newsId;

    public GetNewsBody() {
        id = "get_news_body";
    }

    public GetNewsBody(int newsId) {
        this();
        this.newsId = newsId;
    }
}


