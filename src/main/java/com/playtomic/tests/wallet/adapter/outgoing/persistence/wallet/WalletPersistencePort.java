package com.playtomic.tests.wallet.adapter.outgoing.persistence.wallet;

import com.playtomic.tests.wallet.core.domain.Wallet;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface WalletPersistencePort {

    Optional<Wallet> getWallet(Long id);

    Wallet updateWallet(Wallet wallet);
}
