package com.playtomic.tests.wallet.core.service.wallet;

import com.playtomic.tests.wallet.adapter.incoming.rest.request.ChargeWalletRequest;
import com.playtomic.tests.wallet.adapter.outgoing.rest.StripeService;
import com.playtomic.tests.wallet.adapter.outgoing.rest.responses.Payment;
import com.playtomic.tests.wallet.core.domain.model.Wallet;
import com.playtomic.tests.wallet.core.domain.port.outgoing.WalletPersistencePort;
import com.playtomic.tests.wallet.core.exceptions.wallet.WalletChargeException;
import com.playtomic.tests.wallet.core.exceptions.wallet.WalletNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class ChargeWalletServiceTest {

    private WalletPersistencePort walletPersistencePort;

    private StripeService stripeService;

    private ChargeWalletService chargeWalletService;

    @BeforeEach
    void setup() {
        walletPersistencePort = mock(WalletPersistencePort.class);
        stripeService = mock(StripeService.class);
        chargeWalletService = new ChargeWalletService(walletPersistencePort, stripeService);
    }

    @Test
    void test_chargeWallet_whenAmountTooSmall_throwException() {
        var request = mock(ChargeWalletRequest.class);
        when(request.amount()).thenReturn(new BigDecimal(1));
        assertThrows(WalletChargeException.class,
                () -> chargeWalletService.chargeWallet(request));
    }

    @Test
    void test_chargeWallet_whenWalletNotFound_throwException() {
        var request = mock(ChargeWalletRequest.class);
        when(request.amount()).thenReturn(new BigDecimal(11));
        when(request.walletId()).thenReturn(234L);
        when(walletPersistencePort.getWallet(234L)).thenReturn(Optional.empty());
        assertThrows(WalletNotFoundException.class,
                () -> chargeWalletService.chargeWallet(request));
    }

    @Test
    void test_chargeWallet_whenMeetsConditions_thenProcessCorrectly() {
        var request = mock(ChargeWalletRequest.class);
        var wallet = mock(Wallet.class);
        when(request.amount()).thenReturn(new BigDecimal(11));
        when(walletPersistencePort.getWallet(any())).thenReturn(Optional.of(wallet));
        when(wallet.getAmount()).thenReturn(new BigDecimal(11));
        when(stripeService.charge(any(), any())).thenReturn(mock(Payment.class));
        when(walletPersistencePort.updateWallet(any())).thenReturn(wallet);

        var result = chargeWalletService.chargeWallet(request);
        verify(walletPersistencePort).getWallet(any());
        verify(stripeService).charge(any(), any());
        verify(walletPersistencePort).updateWallet(any());
        verifyNoMoreInteractions(walletPersistencePort, stripeService);
    }

}