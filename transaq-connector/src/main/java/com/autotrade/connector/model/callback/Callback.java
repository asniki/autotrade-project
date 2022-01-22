package com.autotrade.connector.model.callback;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Базовый класс всех колбеков.
 * Содержит строковый идентификатор типа данных.
 * Все колбеки:
 * error
 * authentication
 * markets
 * boards
 * candlekinds
 * securities
 * pits
 * sec_info_upd
 * client
 * positions
 * overnight
 * messages
 * server_status
 * connector_version
 * current_server
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Callback {
    @JsonIgnore
    protected String kind;
}
