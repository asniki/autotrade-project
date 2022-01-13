package com.autotrade.connector.model.command;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Запрос на получение версии модуля XmlConnector.
 * Результатом является структура connector_version.
 */
@JacksonXmlRootElement(localName = "command")
@Builder
@EqualsAndHashCode(callSuper = true)
@Data
public class GetConnectorVersion extends Command {

    public GetConnectorVersion() {
        id = "get_connector_version";
    }
}
