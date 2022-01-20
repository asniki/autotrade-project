package com.autotrade.connector.model.callback;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Базовый класс всех колбеков.
 * Содержит строковый идентификатор типа данных.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Callback {
    @JsonIgnore
    protected String kind;
}
