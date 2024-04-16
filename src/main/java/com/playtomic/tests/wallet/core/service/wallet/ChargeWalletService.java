package com.playtomic.tests.wallet.core.service.wallet;

import com.playtomic.tests.wallet.adapter.incoming.rest.request.ChargeWalletRequest;
import com.playtomic.tests.wallet.adapter.outgoing.rest.StripeService;
import com.playtomic.tests.wallet.core.domain.model.Wallet;
import com.playtomic.tests.wallet.core.domain.port.incoming.wallet.ChargeWalletUseCase;
import com.playtomic.tests.wallet.core.domain.port.outgoing.WalletPersistencePort;
import com.playtomic.tests.wallet.core.exceptions.stripe.StripeAmountTooSmallException;
import com.playtomic.tests.wallet.core.exceptions.wallet.WalletChargeException;
import com.playtomic.tests.wallet.core.exceptions.wallet.WalletNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
@Slf4j
public class ChargeWalletService implements ChargeWalletUseCase {

    private final WalletPersistencePort walletPersistencePort;
    private StripeService stripeService;

    @Override
    public Wallet chargeWallet(ChargeWalletRequest chargeWallet) {
        log.info("Charging wallet {} with amount {}", chargeWallet.walletId(), chargeWallet.amount());
        if (chargeWallet.amount().compareTo(BigDecimal.valueOf(10)) < 0) {
            throw new WalletChargeException("Amount too small");
        }
        return walletPersistencePort.getWallet(chargeWallet.walletId())
                .map(wallet -> {
                    try {
                        stripeService.charge(chargeWallet.creditNumber(), chargeWallet.amount());
                    } catch (StripeAmountTooSmallException e) {
                        log.error("Error stripe charging wallet. Amount too small", e);
                        throw new WalletChargeException("Amount too small");
                    }
                    BigDecimal amountUpdated = wallet.getAmount().add(chargeWallet.amount());
                    wallet.setAmount(amountUpdated);
                    return walletPersistencePort.updateWallet(wallet);
                }).orElseThrow(() -> new WalletNotFoundException());
    }
}
