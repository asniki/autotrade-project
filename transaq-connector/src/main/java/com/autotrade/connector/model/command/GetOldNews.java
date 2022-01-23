package com.autotrade.connector.model.command;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Запрос на получение заголовков более старых новостей.
 * Результатом является структура news_header.
 */
@JacksonXmlRootElement(localName = "command")
@Builder
@EqualsAndHashCode(callSuper = true)
@Data
public class GetOldNews extends Command {

    /** Максимальное количество новостей, которое необходимо получить, не может превышать 100 */
    @JacksonXmlProperty(isAttribute = true, localName = "count")
    private int count;

    public GetOldNews() {
        id = "get_old_news";
    }

    public GetOldNews(int count) {
        this();
        this.count = count;
    }
}


