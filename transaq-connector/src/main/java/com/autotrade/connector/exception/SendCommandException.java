package com.autotrade.connector.exception;

public class SendCommandException extends Exception {

    public SendCommandException(String message) {
        super(message);
    }

    public SendCommandException(String message, Throwable ex) {
        super(message, ex);
    }
}