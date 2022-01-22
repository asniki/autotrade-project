package com.autotrade.connector.model.callback;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalTime;
import java.util.List;

/**
 * Текстовые сообщения
 * Сообщение приходит:
 * как асинхронный ответ на команду connect,
 */
@JacksonXmlRootElement(localName = "messages")
@EqualsAndHashCode(callSuper = true)
@Data
public class Messages extends Callback{
    @JacksonXmlProperty(localName = "message")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Message> items;

    public Messages() {
        this.kind = "messages";
    }

    /**
     * Текстовое сообщения
     */
    @Data
    public static class Message {
        // https://stackoverflow.com/questions/27571377/datetimeformatter-support-for-single-digit-day-of-month-and-month-of-year
        /** Время */
        @JacksonXmlProperty(localName = "date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "[H][HH]:mm:ss")
        private LocalTime time;

        /** Срочное: Y / N */
        @JacksonXmlProperty(localName = "urgent")
        private String urgent;

        /** Отправитель */
        @JacksonXmlProperty(localName = "from")
        private String from;

        /** Текст сообщения, содержит секцию CDATA */
        @JacksonXmlProperty(localName = "text")
        private String text;
    }
}
