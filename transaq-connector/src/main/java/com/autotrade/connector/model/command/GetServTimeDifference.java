package com.autotrade.connector.model.command;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Получить разницу между временем на компьютере пользователя и серверным временем.
 * Результатом является синхронный ответ TimeDifferenceResult.
 */
@JacksonXmlRootElement(localName = "command")
@Builder
@EqualsAndHashCode(callSuper = true)
@Data
public class GetServTimeDifference extends Command {

    public GetServTimeDifference() {
        id = "get_servtime_difference";
    }
}


