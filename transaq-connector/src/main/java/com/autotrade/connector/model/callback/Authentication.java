package com.autotrade.connector.model.callback;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

/**
 * @deprecated
 * Сообщение приходит:
 * как асинхронный ответ на команду connect,
 */
@JacksonXmlRootElement(localName = "authentication")
@Data
public class Authentication {
    @JacksonXmlProperty(isAttribute = true, localName = "status")
    private String status;
}
