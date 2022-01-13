package com.autotrade.connector.model.callback;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

/**
 * Клиентский счет
 */
@JacksonXmlRootElement(localName = "client")
@Data
public class Client {
    /** ID клиента */
    @JacksonXmlProperty(isAttribute = true, localName = "id")
    private String id;

    /**
     * Значение remove="false" означает, что клиент доступен. Значение remove="true" означает, что клиент был удален.
     * В случае удаления клиента никаких свойств клиента передано не будет.
     */
    @JacksonXmlProperty(isAttribute = true, localName = "remove")
    private String remove;

    /** Тип клиента: spot (кассовый),leverage (плечевой), margin_level (маржинальный), mct (клиент ММА) */
    @JacksonXmlProperty(localName = "type")
    private String type;

    /** Валюта фондового портфеля клиента: NA (клиент не имеет фондового портфеля), RUB, EUR, USD */
    @JacksonXmlProperty(localName = "currency")
    private String currency;

    /** Идентификатор рынка, на котором разрешено работать данному клиенту */
    @JacksonXmlProperty(localName = "market")
    private int market;

    /**
     * Код Единого Портфеля, в который включен клиент.
     * Отсутствует, если клиент не включён в юнион.
     */
    @JacksonXmlProperty(localName = "union")
    private String union;

    /**
     * Счет FORTS клиента.
     * Если клиент не имеет счета FORTS.
     */
    @JacksonXmlProperty(localName = "forts_acc")
    private String fortsAccount;
}
