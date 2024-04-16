package com.playtomic.tests.wallet.core.domain.model;

import java.math.BigDecimal;

public record ChargeWallet(Long walletId, String cardNumber, BigDecimal amount) {
}
