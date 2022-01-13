package com.autotrade.connector.model.command;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Установить соединение с сервером.
 * Результатом является структура server_status.
 */
@JacksonXmlRootElement(localName = "command")
@Builder
@EqualsAndHashCode(callSuper = true)
@Data
public class Connect extends Command {
    /** Логин */
    @JacksonXmlProperty(localName = "login")
    private String login;

    /** Пароль */
    @JacksonXmlProperty(localName = "password")
    private String password;

    /** Хост сервера transaq */
    @JacksonXmlProperty(localName = "host")
    private String host;

    /** Порт сервера transaq */
    @JacksonXmlProperty(localName = "port")
    private String port;

    /** Язык системных сообщений: ru / en */
    @JacksonXmlProperty(localName = "language")
    private String language;

    /**
     * Автоматический запрос клиентских позиций на срочном рынке FORTS после каждой клиентской сделки: true / false.
     * По дефолту: true.
     * Использование значения false при активной торговле ускоряет взаимодействие с сервером.
     */
    @JacksonXmlProperty(localName = "autopos")
    private Boolean autoPositions;

    /**
     * Определяет набор данных, передаваемый в структурах positions.money_position и positions.sec_position: true / false.
     * true: в money_position и sec_position для фондового рынка ММВБ будут отдаваться данные по всем регистрам, а также будет присутствовать элемент "register", определяющий учетный регистр позиции.
     * false: в money_position и sec_position для фондового рынка ММВБ будут отдаваться только данные с регистром "T0".
     */
    @JacksonXmlProperty(localName = "micex_registers")
    private Boolean micexRegisters;

    /**
     * Определяет формат элементов типа "Дата и время": true / false.
     * Если true, следующие данные отдаются в форматеDD.MM.YYYY hh:mm:ss.fff:
     * quotations.quotation.time
     * trades.trade.time,
     * alltrades.trade.time,
     * ticks.tick.tradetime,
     * order.time,
     * order.expdate,
     * order.accepttime,
     * order.withdrawtime,
     * order.conditionvalue,
     * order.validafter,
     * order.validbefore,
     * stoporder.validbefore,
     * stoporder.accepttime,
     * stoporder.expdate,
     * stoporder.stoploss.guardtime,
     * stoporder.takeprofit.guardtime,
     * stoporder.withdrawtime
     */
    @JacksonXmlProperty(localName = "milliseconds")
    private Boolean milliseconds;

    /**
     * Определяет таймзону некоторых элементов "Дата и время": true / false.
     * Если true, следующие элементы передаются в UTC:
     * alltrade.time,
     * tick.tradetime,
     * trade.time,
     * quotation.time,
     * candle.date,
     * order.time,
     * order.withdrawtim,
     * order.conditionvalue (если condition="Time" и в нем задано дата-время),
     * order.accepttime,
     * order.validafter,
     * order.validbefore,
     * stoporder.validbefore,
     * stoporder.withdrawtime,
     * stoporder.accepttime,
     * message.date,
     * news_header.time_stamp.
     *
     * Не влияет на следующие структуры: sec_info.mat_date, sec_info.coupon_date, order.expdate
     *
     * Если true, следующие элементы необходимо указывать в UTC:
     * newcondorder.validafter, newcondorder.validbefore, newcondorder.cond_value (если они заданы и не заданы спец.значения validafter=0 или validbefore=0);
     * newstoporder.validfor (если не задано спец.значения validfor=0)
     */
    @JacksonXmlProperty(localName = "utc_time")
    private Boolean utcTime;

    /**
     * Имя файла, в котором будут храниться примечания к заявкам. Если не указать имя файла, по умолчанию будет использоваться файл notes.xml.
     * @deprecated
     */
    @JacksonXmlProperty(localName = "notes_file")
    private String notesFile;

    /** Задается если подключение осуществляется через прокси-сервер */
    @JacksonXmlProperty(localName = "proxy")
    private Proxy proxy;

    /** Период агрегирования данных (частота обращений Коннектора к серверу Transaq) в миллисекундах. Минимальное значение 10 */
    @JacksonXmlProperty(localName = "rqdelay")
    private Integer requestDelay;

    /**
     * Таймаут сессии в секундах
     * Интервал времени, в течении которого коннектор в случае ошибок связи будет выполнять попытки переподключения к серверу без повторного получения списка
     * финансовых инструментов (securities) и других справочников. Если данный параметр не задан, используется значение по-умолчанию равное 120 секундам.
     */
    @JacksonXmlProperty(localName = "session_timeout")
    private Integer sessionTimeout;

    /**
     * Таймаут запроса в секундах
     * Если данный параметр не задан, используется значение по-умолчанию равное 20 секундам.
     * Значение параметра session_timeout должно быть больше значения параметра request_timeout
     */
    @JacksonXmlProperty(localName = "request_timeout")
    private Integer requestTimeout;

    /**
     * Тип криптозащиты, отличный он дефолтного (логин и пароль TRANSAQ)
     * @deprecated
     */
    @JacksonXmlProperty(localName = "csp")
    private String csp;

    /**
     * Период в секундах.
     * Значение отличное от 0 и null, является инструкцией обеспечивать для каждого юниона информирование пользователя о текущих показателях Единого портфеля (структура positions)
     * - всякий раз при возникновении существенных событий (изменение состояния заявок или сделок клиентов юниона), но не реже чем один раз в N секунд
     */
    @JacksonXmlProperty(localName = "push_u_limits")
    private Integer pushUnitedLimits;

    /**
     * Период в секундах.
     * Значение отличное от 0 и null, является инструкцией раз в N секунд информировать пользователя о текущей стоимости позиций, передавая ему массив структур (positions.sec_position),
     * соответствующих множеству удерживаемых клиентом позиций (за исключением позиций FORTS)
     */
    @JacksonXmlProperty(localName = "push_pos_equity")
    private Integer pushPositionsEquity;

    public Connect() {
        id = "connect";
    }

    public Connect(String login, String password, String host, String port, String language, Boolean autoPositions, Boolean micexRegisters,
                   Boolean milliseconds, Boolean utcTime, String notesFile, Proxy proxy, Integer requestDelay, Integer sessionTimeout,
                   Integer requestTimeout, String csp, Integer pushUnitedLimits, Integer pushPositionsEquity) {
        this();
        this.login = login;
        this.password = password;
        this.host = host;
        this.port = port;
        this.language = language;
        this.autoPositions = autoPositions;
        this.micexRegisters = micexRegisters;
        this.milliseconds = milliseconds;
        this.utcTime = utcTime;
        this.notesFile = notesFile;
        this.proxy = proxy;
        this.requestDelay = requestDelay;
        this.sessionTimeout = sessionTimeout;
        this.requestTimeout = requestTimeout;
        this.csp = csp;
        this.pushUnitedLimits = pushUnitedLimits;
        this.pushPositionsEquity = pushPositionsEquity;
    }

    @Data
    public static class Proxy {
        /** Тип прокси-сервера: SOCKS4, SOCKS5, HTTP-CONNECT */
        @JacksonXmlProperty(isAttribute = true, localName = "type")
        private String type;

        /**  Хост */
        @JacksonXmlProperty(isAttribute = true, localName = "addr")
        private String address;

        /** Порт */
        @JacksonXmlProperty(isAttribute = true, localName = "port")
        private String port;

        /** Логин (при необходимости авторизации) */
        @JacksonXmlProperty(isAttribute = true, localName = "login")
        private String login;

        /** Пароль (при необходимости авторизации) */
        @JacksonXmlProperty(isAttribute = true, localName = "password")
        private String password;
    }
}