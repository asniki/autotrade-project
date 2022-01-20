package com.autotrade.connector.model.callback;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Сообщение приходит:
 * как асинхронный ответ на команду connect,
 * @deprecated
 */
@JacksonXmlRootElement(localName = "authentication")
@EqualsAndHashCode(callSuper = true)
@Data
public class Authentication2 extends Callback {
    @JacksonXmlProperty(isAttribute = true, localName = "status")
    private String status;

    public Authentication2() {
        this.kind = "authentication";
    }
}



