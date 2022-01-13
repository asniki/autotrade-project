package com.autotrade.connector.model.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;


/**
 * Стандартный ответ на любую команду кроме:
 * neworder, newcondorder, newstoporder, cancelorder, cancelstoporder, moveorder, get_servtime_difference
 */
@JacksonXmlRootElement(localName = "result")
@Data
public class Result {
    /** true / false */
    @JacksonXmlProperty(isAttribute = true, localName = "success")
    private String success;

    /** Сообщение об ошибке (если success == false) */
    @JacksonXmlProperty(localName = "message")
    private String message;
}
