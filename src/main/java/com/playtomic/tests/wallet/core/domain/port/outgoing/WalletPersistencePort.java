package com.playtomic.tests.wallet.core.domain.port.outgoing;


import com.playtomic.tests.wallet.core.domain.model.Wallet;

import java.util.Optional;

public interface WalletPersistencePort {

    Optional<Wallet> getWallet(Long id);

    Wallet updateWallet(Wallet wallet);
}
