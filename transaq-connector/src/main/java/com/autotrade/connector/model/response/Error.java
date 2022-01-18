package com.autotrade.connector.model.response;

import com.autotrade.connector.model.callback.Callback;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Ответ на любую команду в случае исключительной ситуации
 */
@JacksonXmlRootElement(localName = "error")
@EqualsAndHashCode(callSuper = true)
@Data
public class Error extends Callback {
    /**
     * Сообщение
     */
    @JacksonXmlText
    private String message;

    public Error() {
        this.kind = "error";
    }
}