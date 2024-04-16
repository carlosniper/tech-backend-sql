package com.playtomic.tests.wallet.core.domain.port.incoming.wallet;

import com.playtomic.tests.wallet.core.domain.model.Wallet;

public interface GetSingleWalletUseCase {

    Wallet getWallet(Long id);

}
