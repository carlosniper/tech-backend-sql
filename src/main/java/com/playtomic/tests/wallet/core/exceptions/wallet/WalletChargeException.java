package com.playtomic.tests.wallet.core.exceptions.wallet;

public class WalletChargeException extends RuntimeException {

    private String message;

    public WalletChargeException(String message) {
        super(message);
        this.message = message;
    }
}
