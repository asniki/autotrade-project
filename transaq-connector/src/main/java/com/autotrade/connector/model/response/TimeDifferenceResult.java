package com.autotrade.connector.model.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

@JacksonXmlRootElement(localName = "result")
@EqualsAndHashCode(callSuper = true)
@Data
public class TimeDifferenceResult extends Result {

    /** Разница между временем на компьютере пользователя и серверным временем в секундах */
    @JacksonXmlProperty(isAttribute = true, localName = "diff")
    protected int timeDifference;
}
