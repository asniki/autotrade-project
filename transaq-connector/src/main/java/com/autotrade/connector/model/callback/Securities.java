package com.autotrade.connector.model.callback;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

/**
 * Список инструментов
 * Сообщение приходит:
 * как асинхронный ответ на команду connect,
 * как асинхронный ответ на команду get_securities
 * @apiNote Может приходить не единым блоком, а несколькими, а также в ходе сессии по мере подключения рынков,
 * и динамического получения доступа к отдельным инструментам
 */
@JacksonXmlRootElement(localName = "securities")
@EqualsAndHashCode(callSuper = true)
@Data
public class Securities extends Callback {
    @JacksonXmlProperty(localName = "security")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Security> items;

    public Securities() {
        this.kind = "securities";
    }

    /**
     * Инструмент
     */
    @Data
    public static class Security {
        /** Внутренний код */
        @JacksonXmlProperty(isAttribute = true, localName = "secid")
        private int securityId;

        /** true / false */
        @JacksonXmlProperty(isAttribute = true, localName = "active")
        private String active;

        /** Код инструмента */
        @JacksonXmlProperty(localName = "seccode")
        private String securityCode;

        /** Символ категории (класса) инструмента */
        @JacksonXmlProperty(localName = "instrclass")
        private String instrumentClass;

        /** Идентификатор режима торгов по умолчанию */
        @JacksonXmlProperty(localName = "board")
        private String defaultBoard;

        /** Идентификатор рынка */
        @JacksonXmlProperty(localName = "market")
        private int market;

        /** Наименование бумаги */
        @JacksonXmlProperty(localName = "shortname")
        private String shortName;

        /** Количество десятичных знаков в цене (для режима торгов по умолчанию) */
        @JacksonXmlProperty(localName = "decimals")
        private int defaultDecimals;

        /** Шаг цены (для режима торгов по умолчанию) */
        @JacksonXmlProperty(localName = "minstep")
        private String defaultMinStepHolder;

        /** Шаг цены (для режима торгов по умолчанию) */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal defaultMinStep = (defaultMinStepHolder == null) ? null : new BigDecimal(defaultMinStepHolder);

        /** Размер лота (для режима торгов по умолчанию) */
        @JacksonXmlProperty(localName = "lotsize")
        private Integer defaultLotSize;

        /** Делитель лота (для режима торгов по умолчанию?) */
        @JacksonXmlProperty(localName = "lotdivider")
        private Integer defaultLotDivider;

        /** Стоимость пункта цены (для режима торгов по умолчанию) */
        @JacksonXmlProperty(localName = "point_cost")
        private String defaultPointCostHolder;

        /** Стоимость пункта цены (для режима торгов по умолчанию) */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal defaultPointCost = (defaultPointCostHolder == null) ? null : new BigDecimal(defaultPointCostHolder);

        /** Параметры торгуемых инструментов (для которых active="true") */
        @JacksonXmlProperty(localName = "opmask")
        private OptionsMask optionsMask;

        /** Тип бумаги */
        @JacksonXmlProperty(localName = "sectype")
        private String securityType;

        /** Имя таймзоны инструмента (типа "Russian Standard Time", "USA=Eastern Standard Time") */
        @JacksonXmlProperty(localName = "sec_tz")
        private String securityTimeZone;

        /** 0 - без стакана; 1 - стакан типа OrderBook; 2 - стакан типа Level2 */
        @JacksonXmlProperty(localName = "quotestype")
        private int quotesType;

        /** Код биржи листинга по стандарту ISO */
        @JacksonXmlProperty(localName = "MIC")
        private String mic;

        /** Тикер на бирже листинга */
        @JacksonXmlProperty(localName = "ticker")
        private String ticker;

        /** Валюта цены */
        @JacksonXmlProperty(localName = "currency")
        private String currency;
    }

    /**
     * Параметры торгуемых инструментов (для которых active="true")
     */
    @Data
    public static class OptionsMask {
        /**
         * Использовать кредит: yes / no
         */
        @JacksonXmlProperty(isAttribute = true, localName = "usecredit")
        private String useCredit;

        /**
         * По рынку: yes / no
         */
        @JacksonXmlProperty(isAttribute = true, localName = "bymarket")
        private String byMarket;

        /**
         * По одной цене: yes / no
         */
        @JacksonXmlProperty(isAttribute = true, localName = "nosplit")
        private String noSplit;

        /**
         * Немедленно или отклонить: yes / no; равен immorcancel
         */
        @JacksonXmlProperty(isAttribute = true, localName = "fok")
        private String fok;

        /**
         * Снять остаток: yes / no; равен cancelbalance
         */
        @JacksonXmlProperty(isAttribute = true, localName = "ioc")
        private String ioc;

        /**
         * Немедленно или отклонить: yes / no; равен fok
         */
        @JacksonXmlProperty(isAttribute = true, localName = "immorcancel")
        private String immediatelyOrCancel;

        /**
         * Снять остаток: yes / no; равен ioc
         */
        @JacksonXmlProperty(isAttribute = true, localName = "cancelbalance")
        private String cancelBalance;
    }
}
