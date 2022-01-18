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
 * Позиции клиента
 */
@JacksonXmlRootElement(localName = "positions")
@EqualsAndHashCode(callSuper = true)
@Data
public class Positions2 extends Callback{

    /** Денежные позиции */
    @JacksonXmlProperty(localName = "money_position")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<MoneyPosition> moneyPositions;

    /** Позиции фондового рынка */
    @JacksonXmlProperty(localName = "sec_position")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<SecurityPosition> securityPositions;

    /** Позиции срочного рынка FORTS */
    @JacksonXmlProperty(localName = "forts_position")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<FortsPosition> fortsPositions;

    /** Денежные позиции FORTS */
    @JacksonXmlProperty(localName = "forts_money")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<FortsMoney> fortsMoney;

    /** Залоговые позиции FORTS */
    @JacksonXmlProperty(localName = "forts_collaterals")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<FortsCollaterals> fortsCollaterals;

    /** Лимиты FORTS */
    @JacksonXmlProperty(localName = "spot_limit")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<SpotLimit> spotLimit;

    public Positions2() {
        this.kind = "positions";
    }

    /**
     * Денежная позиция
     */
    @Data
    public static class MoneyPosition {

        /** Код валюты */
        @JacksonXmlProperty(localName = "currency")
        private String currency;

        /** Рынки, на которых можно использовать денежную позицию */
        @JacksonXmlProperty(localName = "market")
        @JacksonXmlElementWrapper(localName = "markets")
        private List<Integer> markets;

        /** Регистр учета (передается только если в команде connect указано поле micex_registers = true) */
        @JacksonXmlProperty(localName = "register")
        private String register;

        /** Код вида средств */
        @JacksonXmlProperty(localName = "asset")
        private String asset;

        /** Идентификатор клиента */
        @JacksonXmlProperty(localName = "client")
        private String client;

        /** Код юниона */
        @JacksonXmlProperty(localName = "union")
        private String union;

        /** Наименование вида средств */
        @JacksonXmlProperty(localName = "shortname")
        private String shortName;

        /** Входящий остаток (сальдо) */
        @JacksonXmlProperty(localName = "saldoin")
        private String incomingBalanceHolder;

        /** Входящий остаток (сальдо) */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal incomingBalance = (incomingBalanceHolder == null) ? null : new BigDecimal(incomingBalanceHolder);

        /** Куплено */
        @JacksonXmlProperty(localName = "bought")
        private String boughtHolder;

        /** Куплено */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal bought = (boughtHolder == null) ? null : new BigDecimal(boughtHolder);

        /** Продано */
        @JacksonXmlProperty(localName = "sold")
        private String soldHolder;

        /** Продано */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal sold = (soldHolder == null) ? null : new BigDecimal(soldHolder);

        /** Текущее сальдо */
        @JacksonXmlProperty(localName = "saldo")
        private String balanceHolder;

        /** Текущее сальдо */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal balance = (balanceHolder == null) ? null : new BigDecimal(balanceHolder);

        /** В заявках на покупку + комиссия */
        @JacksonXmlProperty(localName = "ordbuy")
        private String ordersBuyHolder;

        /** В заявках на покупку + комиссия */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal ordersBuy = (ordersBuyHolder == null) ? null : new BigDecimal(ordersBuyHolder);

        /** В условных заявках на покупку */
        @JacksonXmlProperty(localName = "ordbuycond")
        private String ordersBuyConditionHolder;

        /** В условных заявках на покупку */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal ordersBuyCondition = (ordersBuyConditionHolder == null) ? null : new BigDecimal(ordersBuyConditionHolder);

        /** Сумма списанной комиссии */
        @JacksonXmlProperty(localName = "comission")
        private String commissionHolder;

        /** Сумма списанной комиссии */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal commission = (commissionHolder == null) ? null : new BigDecimal(commissionHolder);
    }

    /**
     * Позиция фондового рынка
     */
    @Data
    public static class SecurityPosition {
        /** Идентификатор инструмента */
        @JacksonXmlProperty(localName = "secid")
        private int securityId;

        /** Внутренний код рынка */
        @JacksonXmlProperty(localName = "market")
        private int market;

        /** Код инструмента */
        @JacksonXmlProperty(localName = "seccode")
        private String securityCode;

        /** Регистр учета (передается только если в команде connect указано поле micex_registers = true) */
        @JacksonXmlProperty(localName = "register")
        private String register;

        /** Идентификатор клиента */
        @JacksonXmlProperty(localName = "client")
        private String client;

        /** Код юниона */
        @JacksonXmlProperty(localName = "union")
        private String union;

        /** Наименование инструмента */
        @JacksonXmlProperty(localName = "shortname")
        private String shortName;

        /** Входящий остаток */
        @JacksonXmlProperty(localName = "saldoin")
        private long incomingBalance;

        /** Неснижаемый остаток */
        @JacksonXmlProperty(localName = "saldomin")
        private long minimalBalance;

        /** Куплено */
        @JacksonXmlProperty(localName = "bought")
        private long bought;

        /** Продано */
        @JacksonXmlProperty(localName = "sold")
        private long sold;

        /** Текущее сальдо */
        @JacksonXmlProperty(localName = "saldo")
        private long balance;

        /** В заявках на покупку */
        @JacksonXmlProperty(localName = "ordbuy")
        private long ordersBuy;

        /** В заявках на продажу */
        @JacksonXmlProperty(localName = "ordsell")
        private long ordersSell;

        /** Текущая оценка стоимости позиции в валюте инструмента */
        @JacksonXmlProperty(localName = "amount")
        private String amountHolder;

        /** Текущая оценка стоимости позиции в валюте инструмента */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal amount = (amountHolder == null) ? null : new BigDecimal(amountHolder);

        /** Текущая оценка стоимости позиции в рублях */
        @JacksonXmlProperty(localName = "equity")
        private String equityHolder;

        /** Текущая оценка стоимости позиции в рублях */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal equity = (equityHolder == null) ? null : new BigDecimal(equityHolder);
    }

    /**
     * Позиция срочного рынка FORTS
     */
    @Data
    public static class FortsPosition {
        /** Идентификатор инструмента */
        @JacksonXmlProperty(localName = "secid")
        private int securityId;

        /** Рынки, на которых можно использовать позицию */
        @JacksonXmlProperty(localName = "market")
        @JacksonXmlElementWrapper(localName = "markets")
        private List<Integer> markets;

        /** Код инструмента */
        @JacksonXmlProperty(localName = "seccode")
        private String securityCode;

        /** Идентификатор клиента */
        @JacksonXmlProperty(localName = "client")
        private String client;

        /**  Код юниона */
        @JacksonXmlProperty(localName = "union")
        private String union;

        /**  Входящая позиция по инструменту */
        @JacksonXmlProperty(localName = "startnet")
        private int startNet;

        /** В заявках на покупку */
        @JacksonXmlProperty(localName = "openbuys")
        private int openBuys;

        /** В заявках на продажу */
        @JacksonXmlProperty(localName = "opensells")
        private int openSells;

        /** Текущая позиция по инструменту */
        @JacksonXmlProperty(localName = "totalnet")
        private int totalNet;

        /** Куплено */
        @JacksonXmlProperty(localName = "todaybuy")
        private int todayBuy;

        /** Продано */
        @JacksonXmlProperty(localName = "todaysell")
        private int todaySell;

        /** Маржа для маржируемых опционов */
        @JacksonXmlProperty(localName = "optmargin")
        private String optMarginHolder;

        /** Маржа для маржируемых опционов */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal optMargin = (optMarginHolder == null) ? null : new BigDecimal(optMarginHolder);

        /** Вариационная маржа */
        @JacksonXmlProperty(localName = "varmargin")
        private String varMarginHolder;

        /** Вариационная маржа */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal varMargin = (varMarginHolder == null) ? null : new BigDecimal(varMarginHolder);

        /** Опционов в заявках на исполнение */
        @JacksonXmlProperty(localName = "expirationpos")
        private long expirationPos;

        /** Объем использованного спот-лимита на продажу */
        @JacksonXmlProperty(localName = "usedsellspotlimit")
        private String usedSellSpotLimitHolder;

        /** Объем использованного спот-лимита на продажу */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal usedSellSpotLimit = (usedSellSpotLimitHolder == null) ? null : new BigDecimal(usedSellSpotLimitHolder);

        /** Текущий спот-лимит на продажу, установленный Брокером */
        @JacksonXmlProperty(localName = "sellspotlimit")
        private String sellSpotLimitHolder;

        /** Текущий спот-лимит на продажу, установленный Брокером */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal sellSpotLimit = (sellSpotLimitHolder == null) ? null : new BigDecimal(sellSpotLimitHolder);

        /** Нетто-позиция по всем инструментам данного спота */
        @JacksonXmlProperty(localName = "netto")
        private String netHolder;

        /** Нетто-позиция по всем инструментам данного спота */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal net = (netHolder == null) ? null : new BigDecimal(netHolder);

        /** Коэффициент ГО (гарантийного обеспечения) для спота */
        @JacksonXmlProperty(localName = "kgo")
        private String kgoHolder;

        /** Коэффициент ГО (гарантийного обеспечения) для спота */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal kgo = (kgoHolder == null) ? null : new BigDecimal(kgoHolder);
    }

    /**
     * Денежная позиция FORTS
     */
    @Data
    public static class FortsMoney {
        /** Идентификатор клиента */
        @JacksonXmlProperty(localName = "client")
        private String client;

        /** Код юниона */
        @JacksonXmlProperty(localName = "union")
        private String union;

        /** Рынки, на которых можно использовать позицию */
        @JacksonXmlProperty(localName = "market")
        @JacksonXmlElementWrapper(localName = "markets")
        private List<Integer> markets;

        /** Наименование вида средств */
        @JacksonXmlProperty(localName = "shortname")
        private String shortName;

        /** Текущие */
        @JacksonXmlProperty(localName = "current")
        private String currentHolder;

        /** Текущие */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal current = (currentHolder == null) ? null : new BigDecimal(currentHolder);

        /** Заблокировано */
        @JacksonXmlProperty(localName = "blocked")
        private String blockedHolder;

        /** Заблокировано */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal blocked = (blockedHolder == null) ? null : new BigDecimal(blockedHolder);

        /** Свободные */
        @JacksonXmlProperty(localName = "free")
        private String freeHolder;

        /** Свободные */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal free = (freeHolder == null) ? null : new BigDecimal(freeHolder);

        /** Опер. Маржа */
        @JacksonXmlProperty(localName = "varmargin")
        private String varMarginHolder;

        /** Опер. Маржа */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal varMargin = (varMarginHolder == null) ? null : new BigDecimal(varMarginHolder);
    }

    /**
     * Залоговая позиция FORTS
     */
    @Data
    public static class FortsCollaterals {
        /** Идентификатор клиента */
        @JacksonXmlProperty(localName = "client")
        private String client;

        /** Код юниона */
        @JacksonXmlProperty(localName = "union")
        private String union;

        /** Рынки, на которых можно использовать позицию */
        @JacksonXmlProperty(localName = "market")
        @JacksonXmlElementWrapper(localName = "markets")
        private List<Integer> markets;

        /** Наименование вида средств */
        @JacksonXmlProperty(localName = "shortname")
        private String shortName;

        /** Текущие */
        @JacksonXmlProperty(localName = "current")
        private String currentHolder;

        /** Текущие */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal current = (currentHolder == null) ? null : new BigDecimal(currentHolder);

        /** Заблокировано */
        @JacksonXmlProperty(localName = "blocked")
        private String blockedHolder;

        /** Заблокировано */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal blocked = (blockedHolder == null) ? null : new BigDecimal(blockedHolder);

        /** Свободные */
        @JacksonXmlProperty(localName = "free")
        private String freeHolder;

        /** Свободные */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal free = (freeHolder == null) ? null : new BigDecimal(freeHolder);
    }

    /**
     * Лимиты FORTS
     */
    @Data
    public static class SpotLimit {
        /** Идентификатор клиента */
        @JacksonXmlProperty(localName = "client")
        private String client;

        /** Код юниона */
        @JacksonXmlProperty(localName = "union")
        private String union;

        /** Рынки, на которых можно использовать позицию */
        @JacksonXmlProperty(localName = "market")
        @JacksonXmlElementWrapper(localName = "markets")
        private List<Integer> markets;

        /** Наименование вида средств */
        @JacksonXmlProperty(localName = "shortname")
        private String shortName;

        /** Текущий лимит */
        @JacksonXmlProperty(localName = "buylimit")
        private String buyLimitHolder;

        /** Текущий лимит */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal buyLimit = (buyLimitHolder == null) ? null : new BigDecimal(buyLimitHolder);

        /** Заблокировано лимита */
        @JacksonXmlProperty(localName = "buylimitused")
        private String buyLimitUsedHolder;

        /** Заблокировано лимита */
        @JsonIgnore
        @Getter(lazy = true)
        private final BigDecimal buyLimitUsed = (buyLimitUsedHolder == null) ? null : new BigDecimal(buyLimitUsedHolder);
    }
}
