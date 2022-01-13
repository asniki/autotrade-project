package com.autotrade.connector.model.command;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * Базовый класс команды
 */
@Data
public class Command {
    /** ID команды */
    @JacksonXmlProperty(isAttribute = true, localName = "id")
    protected String id;
}
