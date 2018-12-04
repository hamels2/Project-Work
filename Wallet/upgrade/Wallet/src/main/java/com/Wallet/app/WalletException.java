package com.Wallet.app;

public class WalletException extends Exception{

    public WalletException(String message) {
        super(message);
    }

    public WalletException(String message, Throwable throwable) {
        super(message, throwable);
    }
}