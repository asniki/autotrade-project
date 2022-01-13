package com.autotrade.connector.model.callback;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.Data;

/**
 * Состояние сервера.
 * Сообщение приходит:
 * как асинхронный ответ на команду connect,
 * при изменении состояния соединения с сервером в процессе работы,
 * как асинхронный ответ на команду server_status.
 */
@JacksonXmlRootElement(localName = "server_status")
@Data
public class ServerStatus {
    /** Версия системы */
    @JacksonXmlProperty(isAttribute = true, localName = "sys_ver")
    private Integer systemVersion;

    /** Билд сервера */
    @JacksonXmlProperty(isAttribute = true, localName = "build")
    private Integer build;

    /** Имя таймзоны сервера */
    @JacksonXmlProperty(isAttribute = true, localName = "server_tz")
    private String serverTimeZone;

    /** ID сервера, с которым в данный момент работает коннектор */
    @JacksonXmlProperty(isAttribute = true, localName = "id")
    private Integer id;

    /** true / false / error */
    @JacksonXmlProperty(isAttribute = true, localName = "connected")
    private String connected;

    /** true, если коннектор пытается восстановить потерянное соединение с сервером / атрибут отсутствует */
    @JacksonXmlProperty(isAttribute = true, localName = "recover")
    private String recover;

    /** Сообщение об ошибке при connected="error" */
    @JacksonXmlText
    private String message;
}

