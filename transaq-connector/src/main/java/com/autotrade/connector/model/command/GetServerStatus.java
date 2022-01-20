package com.autotrade.connector.model.command;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Получить информацию о текущем состоянии соединения с Сервером.
 * Результатом является структура server_status.
 */
@JacksonXmlRootElement(localName = "command")
@Builder
@EqualsAndHashCode(callSuper = true)
@Data
public class GetServerStatus extends Command {

    public GetServerStatus() {
        id = "server_status";
    }
}


