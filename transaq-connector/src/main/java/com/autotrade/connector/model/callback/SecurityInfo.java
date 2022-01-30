package com.autotrade.connector.model.callback;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Информация по инструменту
 * Сообщение приходит:
 * как асинхронный ответ на команду get_securities_info.
 * Элемент не приходит в колбеке, если соответствующее поле не пришло от сервера
 */
@JacksonXmlRootElement(localName = "sec_info")
@EqualsAndHashCode(callSuper = true)
@Data
public class SecurityInfo extends Callback {

    /** Идентификатор инструмента */
    @JacksonXmlProperty(isAttribute = true, localName = "secid")
    private int securityId;

    /** Полное наименование инструмента */
    @JacksonXmlProperty(localName = "secname")
    private String securityName;

    /** Код инструмента */
    @JacksonXmlProperty(localName = "seccode")
    private String securityCode;

    /** Внутренний код рынка */
    @JacksonXmlProperty(localName = "market")
    private int market;

    /** Единицы измерения цены */
    @JacksonXmlProperty(localName = "pname")
    private String priceName;

    /** Дата погашения */
    @JacksonXmlProperty(localName = "mat_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private LocalDate maturityDate;

    /** Цена последнего клиринга (только FORTS) */
    @JacksonXmlProperty(localName = "clearing_price")
    private String clearingPriceHolder;

    /** Цена последнего клиринга (только FORTS) */
    @JsonIgnore
    @Getter(lazy = true)
    private final BigDecimal clearingPrice = (clearingPriceHolder == null) ? null : new BigDecimal(clearingPriceHolder);

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

    /** Текущий НКД (Накопленный купонный доход), руб */
    @JacksonXmlProperty(localName = "accruedint")
    private String accruedInterestHolder;

    /** Текущий НКД (Накопленный купонный доход), руб */
    @JsonIgnore
    @Getter(lazy = true)
    private final BigDecimal accruedInterest = (accruedInterestHolder == null) ? null : new BigDecimal(accruedInterestHolder);

    /** Размер купона, руб */
    @JacksonXmlProperty(localName = "coupon_value")
    private String couponValueHolder;

    /** Размер купона, руб */
    @JsonIgnore
    @Getter(lazy = true)
    private final BigDecimal couponValue = (couponValueHolder == null) ? null : new BigDecimal(couponValueHolder);

    /** Дата погашения купона */
    @JacksonXmlProperty(localName = "coupon_date")
    private String couponDate;                              //TODO date

    /** Период выплаты купона, дни */
    @JacksonXmlProperty(localName = "coupon_period")
    private Integer couponPeriod;

    /** Номинальная стоимость облигации или акции, руб */
    @JacksonXmlProperty(localName = "facevalue")
    private String faceValueHolder;

    /** Номинальная стоимость облигации или акции, руб */
    @JsonIgnore
    @Getter(lazy = true)
    private final BigDecimal faceValue = (faceValueHolder == null) ? null : new BigDecimal(faceValueHolder);

    /** Тип опциона Call(C) / Put(P) */
    @JacksonXmlProperty(localName = "put_call")
    private String putCall;

    /** Стоимость пункта цены */
    @JacksonXmlProperty(localName = "point_cost")
    private String pointCostHolder;

    /** Стоимость пункта цены */
    @JsonIgnore
    @Getter(lazy = true)
    private final BigDecimal pointCost = (pointCostHolder == null) ? null : new BigDecimal(pointCostHolder);

    /** Базовое ГО (гарантийное обеспечение) под покупку маржируемого опциона */
    @JacksonXmlProperty(localName = "bgo_buy")
    private String bgoBuyHolder;

    /** Базовое ГО (гарантийное обеспечение) под покупку маржируемого опциона */
    @JsonIgnore
    @Getter(lazy = true)
    private final BigDecimal bgoBuy = (bgoBuyHolder == null) ? null : new BigDecimal(bgoBuyHolder);

    /** Маржинальный(M) / премия(P) */
    @JacksonXmlProperty(localName = "opt_type")
    private String optionType;

    /** Количество базового актива (FORTS) */
    @JacksonXmlProperty(localName = "lot_volume")
    private Integer lotVolume;

    /** Международный идентификационный код инструмента (International Securities Identification Number) */
    @JacksonXmlProperty(localName = "isin")
    private String isin;

    /** Номер государственной регистрации инструмента */
    @JacksonXmlProperty(localName = "regnumber")
    private String registerNumber;

    /** Цена досрочного выкупа облигации */
    @JacksonXmlProperty(localName = "buybackprice")
    private String buybackPriceHolder;

    /** Цена досрочного выкупа облигации */
    @JsonIgnore
    @Getter(lazy = true)
    private final BigDecimal buybackPrice = (buybackPriceHolder == null) ? null : new BigDecimal(buybackPriceHolder);

    /** Дата досрочного выкупа облигации */
    @JacksonXmlProperty(localName = "buybackdate")
    private String buybackDate;                 //TODO date

    /** Валюта расчетов */
    @JacksonXmlProperty(localName = "currencyid")
    private String currencyId;


    public SecurityInfo() {
        this.kind = "sec_info";
    }
}
