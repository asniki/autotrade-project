package com.autotrade.connector.model.callback;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Версия коннектора (библиотеки)
 */
@JacksonXmlRootElement(localName = "connector_version")
@EqualsAndHashCode(callSuper = true)
@Data
public class ConnectorVersion2 extends Callback{
    /** Номер версии */
    @JacksonXmlText
//    @JacksonXmlElementWrapper(useWrapping = false)
    private String version;

    public ConnectorVersion2() {
        this.kind = "connector_version";
    }
}
