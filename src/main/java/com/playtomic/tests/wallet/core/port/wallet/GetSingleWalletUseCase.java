package com.playtomic.tests.wallet.core.port.wallet;

import com.playtomic.tests.wallet.core.domain.Wallet;

import java.util.UUID;

public interface GetSingleWalletUseCase {

    Wallet getWallet(Long id);

}
