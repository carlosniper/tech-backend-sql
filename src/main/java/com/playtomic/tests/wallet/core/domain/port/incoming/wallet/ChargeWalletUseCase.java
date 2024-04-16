package com.playtomic.tests.wallet.core.domain.port.incoming.wallet;

import com.playtomic.tests.wallet.adapter.incoming.rest.request.ChargeWalletRequest;
import com.playtomic.tests.wallet.core.domain.model.Wallet;

public interface ChargeWalletUseCase {

    Wallet chargeWallet(ChargeWalletRequest chargeWallet);
}
