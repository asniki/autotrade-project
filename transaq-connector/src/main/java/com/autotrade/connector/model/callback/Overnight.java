package com.autotrade.connector.model.callback;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Режим кредитования
 * @deprecated
 */
@JacksonXmlRootElement(localName = "overnight")
@Data
public class Overnight {
    /** Ночной или дневной режим кредитования: true / false */
    @JacksonXmlProperty(isAttribute = true, localName = "status")
    private String status;
}
