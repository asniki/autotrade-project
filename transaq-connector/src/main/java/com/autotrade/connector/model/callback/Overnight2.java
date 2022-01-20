package com.autotrade.connector.model.callback;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Режим кредитования
 * Сообщение приходит:
 * как асинхронный ответ на команду connect,
 * @deprecated
 */
@JacksonXmlRootElement(localName = "overnight")
@EqualsAndHashCode(callSuper = true)
@Data
public class Overnight2 extends Callback {
    /** Ночной или дневной режим кредитования: true / false */
    @JacksonXmlProperty(isAttribute = true, localName = "status")
    private String status;

    public Overnight2() {
        this.kind = "overnight";
    }
}
