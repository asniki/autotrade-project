package com.autotrade.connector.model.callback;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

@JacksonXmlRootElement(localName = "authentication")
@Data
public class Authentication {
    @JacksonXmlProperty(isAttribute = true, localName = "status")
    private String status;
}
