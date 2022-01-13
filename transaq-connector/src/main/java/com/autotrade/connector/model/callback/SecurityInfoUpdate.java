package com.autotrade.connector.model.callback;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * Обновление информации по инструменту
 * Элемент не приходит в колбеке, если соответствующее поле не пришло от сервера
 */
@JacksonXmlRootElement(localName = "sec_info_upd")
@Data
public class SecurityInfoUpdate {
    /** Идентификатор бумаги */
    @JacksonXmlProperty(localName = "secid")
    private int securityId;

    /** Код инструмента */
    @JacksonXmlProperty(localName = "seccode")
    private String securityCode;

    /** Внутренний код рынка */
    @JacksonXmlProperty(localName = "market")
    private int market;

    /** ГО (гарантийное обеспечение) покупателя (фьючерсы FORTS, руб.) */
    @JacksonXmlProperty(localName = "buy_deposit")
    private String buyDepositHolder;

    /** ГО (гарантийное обеспечение) покупателя (фьючерсы FORTS, руб.) */
    @JsonIgnore
    @Getter(lazy = true)
    private final BigDecimal buyDeposit = (buyDepositHolder == null) ? null : new BigDecimal(buyDepositHolder);

    /** ГО (гарантийное обеспечение) продавца (фьючерсы FORTS, руб.) */
    @JacksonXmlProperty(localName = "sell_deposit")
    private String sellDepositHolder;

    /** ГО (гарантийное обеспечение) продавца (фьючерсы FORTS, руб.) */
    @JsonIgnore
    @Getter(lazy = true)
    private final BigDecimal sellDeposit = (sellDepositHolder == null) ? null : new BigDecimal(sellDepositHolder);

    /** ГО (гарантийное обеспечение) покрытой позиции (опционы FORTS, руб.) */
    @JacksonXmlProperty(localName = "bgo_c")
    private String bgoCoveredHolder;

    /** ГО (гарантийное обеспечение) покрытой позиции (опционы FORTS, руб.) */
    @JsonIgnore
    @Getter(lazy = true)
    private final BigDecimal bgoCovered = (bgoCoveredHolder == null) ? null : new BigDecimal(bgoCoveredHolder);

    /** ГО (гарантийное обеспечение) непокрытой позиции (опционы FORTS, руб.) */
    @JacksonXmlProperty(localName = "bgo_nc")
    private String bgoNotCoveredHolder;

    /** ГО (гарантийное обеспечение) непокрытой позиции (опционы FORTS, руб.) */
    @JsonIgnore
    @Getter(lazy = true)
    private final BigDecimal bgoNotCovered = (bgoNotCoveredHolder == null) ? null : new BigDecimal(bgoNotCoveredHolder);

    /** Базовое ГО (гарантийное обеспечение) под покупку маржируемого опциона */
    @JacksonXmlProperty(localName = "bgo_buy")
    private String bgoBuyHolder;

    /** Базовое ГО (гарантийное обеспечение) под покупку маржируемого опциона */
    @JsonIgnore
    @Getter(lazy = true)
    private final BigDecimal bgoBuy = (bgoBuyHolder == null) ? null : new BigDecimal(bgoBuyHolder);

    /** Стоимость пункта цены */
    @JacksonXmlProperty(localName = "point_cost")
    private String pointCostHolder;

    /** Стоимость пункта цены */
    @JsonIgnore
    @Getter(lazy = true)
    private final BigDecimal pointCost = (pointCostHolder == null) ? null : new BigDecimal(pointCostHolder);

    /** Минимальная цена (только FORTS) */
    @JacksonXmlProperty(localName = "minprice")
    private String minPriceHolder;

    /** Минимальная цена (только FORTS) */
    @JsonIgnore
    @Getter(lazy = true)
    private final BigDecimal minPrice = (minPriceHolder == null) ? null : new BigDecimal(minPriceHolder);

    /** Максимальная цена (только FORTS) */
    @JacksonXmlProperty(localName = "maxprice")
    private String maxPriceHolder;

    /** Максимальная цена (только FORTS) */
    @JsonIgnore
    @Getter(lazy = true)
    private final BigDecimal maxPrice = (maxPriceHolder == null) ? null : new BigDecimal(maxPriceHolder);
}
