package com.autotrade.connector.model.callback;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

/**
 * Параметры инструментов в разных режимах торгов
 */
@JacksonXmlRootElement(localName = "pits")
@Data
public class Pits {
    @JacksonXmlProperty(localName = "pit")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Pit> items;

    /**
     * Параметры инструмента в режиме торгов
     */
    @Data
    public static class Pit {
        /** Код инструмента */
        @JacksonXmlProperty(isAttribute = true, localName = "seccode")
        private String securityCode;

        /** Идентификатор режима торгов */
        @JacksonXmlProperty(isAttribute = true, localName = "board")
        private String board;

        /** Идентификатор рынка */
        @JacksonXmlProperty(localName = "market")
        private int market;

        /** Количество десятичных знаков в цене */
        @JacksonXmlProperty(localName = "decimals")
        private int decimals;

        /** Шаг цены */
        @JacksonXmlProperty(localName = "minstep")
        private String minStepHolder;

        /** Шаг цены */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal minStep = (minStepHolder == null) ? null : new BigDecimal(minStepHolder);

        /** Размер лота */
        @JacksonXmlProperty(localName = "lotsize")
        private int lotSize;

        /** Делитель лота */
        @JacksonXmlProperty(localName = "lotdivider")
        private int lotDivider;

        /** Стоимость пункта цены */
        @JacksonXmlProperty(localName = "point_cost")
        private String pointCostHolder;

        /** Стоимость пункта цены */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal pointCost = (pointCostHolder == null) ? null : new BigDecimal(pointCostHolder);
    }
}
