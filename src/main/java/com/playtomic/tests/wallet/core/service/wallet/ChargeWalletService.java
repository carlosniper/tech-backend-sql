package com.playtomic.tests.wallet.core.service.wallet;

import com.playtomic.tests.wallet.adapter.incoming.rest.request.ChargeWalletRequest;
import com.playtomic.tests.wallet.adapter.outgoing.persistence.wallet.WalletPersistencePort;
import com.playtomic.tests.wallet.adapter.outgoing.rest.StripeService;
import com.playtomic.tests.wallet.adapter.outgoing.rest.responses.Payment;
import com.playtomic.tests.wallet.core.domain.Wallet;
import com.playtomic.tests.wallet.core.exceptions.stripe.StripeAmountTooSmallException;
import com.playtomic.tests.wallet.core.exceptions.wallet.WalletChargeException;
import com.playtomic.tests.wallet.core.exceptions.wallet.WalletNotFoundException;
import com.playtomic.tests.wallet.core.port.wallet.ChargeWalletUseCase;
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

    /*Optional<Wallet> wallet = walletPersistencePort.getWallet(chargeWallet.walletId());
        if(wallet.isPresent()) {
            log.info("Wallet found!");
            try {
                log.info("Calling stripe to charge wallet");
                Payment payment = stripeService.charge(chargeWallet.creditNumber(), chargeWallet.amount());
            } catch (StripeAmountTooSmallException e) {
                log.error("Error stripe charging wallet. Amount too small", e);
                throw new WalletChargeException("Amount too small");
            }
            Wallet updateWallet = wallet.get();
            BigDecimal amountUpdated = updateWallet.getAmount().add(chargeWallet.amount());
            updateWallet.setAmount(amountUpdated);
            return walletPersistencePort.updateWallet(updateWallet);
        }
        log.error("Wallet not found");
        throw new WalletNotFoundException();*/
}
