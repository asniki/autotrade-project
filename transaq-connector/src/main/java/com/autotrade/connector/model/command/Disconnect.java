package com.autotrade.connector.model.command;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Завершить подключение к Серверу.
 * Результатом является структура server_status.
 */
@JacksonXmlRootElement(localName = "command")
@Builder
@EqualsAndHashCode(callSuper = true)
@Data
public class Disconnect extends Command {

    public Disconnect() {
        id = "disconnect";
    }
}


