package com.Wallet.app;

public class TransactionException extends Exception{

    public TransactionException(String message) {
        super(message);
    }

    public TransactionException(String message, Throwable throwable) {
        super(message, throwable);
    }
}