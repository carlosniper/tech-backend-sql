package com.playtomic.tests.wallet.adapter.incoming.rest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ChargeWalletRequest(
        @NotNull
        Long walletId,
        @NotBlank
        String creditNumber,
        @NotNull
        BigDecimal amount) {
}
