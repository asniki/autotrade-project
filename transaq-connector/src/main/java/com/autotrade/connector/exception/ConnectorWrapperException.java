package com.autotrade.connector.exception;

public class ConnectorWrapperException extends Exception {

    public ConnectorWrapperException(String message) {
        super(message);
    }

    public ConnectorWrapperException(String message, Throwable ex) {
        super(message, ex);
    }
}
