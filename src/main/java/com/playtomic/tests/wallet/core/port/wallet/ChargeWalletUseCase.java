package com.playtomic.tests.wallet.core.port.wallet;

import com.playtomic.tests.wallet.adapter.incoming.rest.request.ChargeWalletRequest;
import com.playtomic.tests.wallet.core.domain.Wallet;

public interface ChargeWalletUseCase {

    Wallet chargeWallet(ChargeWalletRequest chargeWallet);
}
