package com.autotrade.connector.model.command;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * Базовый класс команды.
 * Все команды:
 * connect
 * disconnect
 * get_connector_version
 * server_status
 * get_securities
 * get_server_id
 * get_markets
 * get_servtime_difference
 */
@Data
public class Command {
    /** ID команды */
    @JacksonXmlProperty(isAttribute = true, localName = "id")
    protected String id;
}
