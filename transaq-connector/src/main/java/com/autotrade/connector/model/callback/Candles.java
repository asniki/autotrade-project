package com.autotrade.connector.model.callback;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Исторические данные
 * Сообщение приходит:
 * как асинхронный ответ на команду gethistorydata
 */
@JacksonXmlRootElement(localName = "candles")
@EqualsAndHashCode(callSuper = true)
@Data
public class Candles extends Callback {
    /** Идентификатор инструмента */
    @JacksonXmlProperty(isAttribute = true, localName = "secid")
    private int securityId;

    /** Идентификатор периода */
    @JacksonXmlProperty(isAttribute = true, localName = "period")
    private int period;

    /**
     * Показывает, осталась ли еще история.
     * 0 - данных больше нет (дочерпали до дна)
     * 1 - заказанное количество выдано (если надо еще - делать еще запрос)
     * 2 - продолжение следует (будет еще порция)
     * 3 - требуемые данные недоступны (есть смысл попробовать запросить позже)
     * */
    @JacksonXmlProperty(isAttribute = true, localName = "status")
    private int status;

    /** Идентификатор режима торгов */
    @JacksonXmlProperty(isAttribute = true, localName = "board")
    private String board;

    /** Код инструмента */
    @JacksonXmlProperty(isAttribute = true, localName = "seccode")
    private String securityCode;

    @JacksonXmlProperty(localName = "candle")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Candle> items;

    public Candles() {
        this.kind = "candles";
    }


    /**
     * Свеча
     */
    @Data
    public static class Candle {
        /** Дата */
        @JacksonXmlProperty(isAttribute = true, localName = "date")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy HH:mm:ss.SSS")
        private LocalDateTime date;

        /** Цена открытия */
        @JacksonXmlProperty(isAttribute = true, localName = "open")
        private String openHolder;

        /** Цена открытия */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal open = (openHolder == null) ? null : new BigDecimal(openHolder);

        /** Максимальная цена */
        @JacksonXmlProperty(isAttribute = true, localName = "high")
        private String highHolder;

        /** Максимальная цена */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal high = (highHolder == null) ? null : new BigDecimal(highHolder);

        /** Минимальная цена */
        @JacksonXmlProperty(isAttribute = true, localName = "low")
        private String lowHolder;

        /** Минимальная цена */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal low = (lowHolder == null) ? null : new BigDecimal(lowHolder);

        /** Цена закрытия */
        @JacksonXmlProperty(isAttribute = true, localName = "close")
        private String closeHolder;

        /** Цена закрытия */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal close = (closeHolder == null) ? null : new BigDecimal(closeHolder);

        /** Объем */
        @JacksonXmlProperty(isAttribute = true, localName = "volume")
        private int volume;

        /** Открытый интерес. Только для фьючерсов и опционов. */
        @JacksonXmlProperty(isAttribute = true, localName = "oi")
        private Integer openInterest;
    }
}
