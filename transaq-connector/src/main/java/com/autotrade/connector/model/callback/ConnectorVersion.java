package com.autotrade.connector.model.callback;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.Data;

/**
 * Версия коннектора (библиотеки)
 * Сообщение приходит:
 * как асинхронный ответ на команду get_connector_version,
 */
@JacksonXmlRootElement(localName = "connector_version")
@Data
public class ConnectorVersion {
    /** Номер версии */
    @JacksonXmlText
    private String version;
}
