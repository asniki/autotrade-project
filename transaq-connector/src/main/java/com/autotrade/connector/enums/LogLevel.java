package com.autotrade.connector.enums;

public enum LogLevel {
    MINIMUM(1),
    DEFAULT(2),
    MAXIMUM(3);

    public final int value;

    LogLevel(int value) {
        this.value = value;
    }
}
