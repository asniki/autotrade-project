package com.autotrade.connector.model.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.Data;

/**
 * Ответ на любую команду в случае исключительной ситуации
 */
@JacksonXmlRootElement(localName = "error")
@Data
public class Error {
    /**
     * Сообщение
     */
    @JacksonXmlText
    private String message;
}