package com.playtomic.tests.wallet.core.service.wallet;

import com.playtomic.tests.wallet.core.domain.model.Wallet;
import com.playtomic.tests.wallet.core.domain.port.incoming.wallet.GetSingleWalletUseCase;
import com.playtomic.tests.wallet.core.domain.port.outgoing.WalletPersistencePort;
import com.playtomic.tests.wallet.core.exceptions.wallet.WalletNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class GetWalletService implements GetSingleWalletUseCase {

    private final WalletPersistencePort walletPersistencePort;

    @Override
    public Wallet getWallet(Long id) {
        return walletPersistencePort.getWallet(id).orElseThrow(() -> new WalletNotFoundException());
    }
}
