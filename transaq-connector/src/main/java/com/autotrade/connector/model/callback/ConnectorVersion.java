package com.autotrade.connector.model.callback;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.Data;

/**
 * Версия коннектора (библиотеки)
 */
@JacksonXmlRootElement(localName = "connector_version")
@Data
public class ConnectorVersion {
    /** Номер версии */
    @JacksonXmlText
    private String version;
}
