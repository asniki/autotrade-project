package com.autotrade.connector.model.callback;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Идентификатор сервера
 * Сообщение приходит:
 * как асинхронный ответ на команду get_server_id
 */
@JacksonXmlRootElement(localName = "current_server")
@EqualsAndHashCode(callSuper = true)
@Data
public class CurrentServer extends Callback{

    /** Идентификатор сервера, с которым установлено текущее соединение */
    @JacksonXmlProperty(isAttribute = true, localName = "id")
    private int id;

    public CurrentServer() {
        this.kind = "current_server";
    }
}
