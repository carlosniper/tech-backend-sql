package com.playtomic.tests.wallet.core.domain;

import java.math.BigDecimal;

public record ChargeWallet(Long walletId, String cardNumber, BigDecimal amount) {
}
