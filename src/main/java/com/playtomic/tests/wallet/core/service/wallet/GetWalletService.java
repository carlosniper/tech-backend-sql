package com.playtomic.tests.wallet.core.service.wallet;

import com.playtomic.tests.wallet.adapter.outgoing.persistence.wallet.WalletPersistencePort;
import com.playtomic.tests.wallet.core.domain.Wallet;
import com.playtomic.tests.wallet.core.exceptions.wallet.WalletNotFoundException;
import com.playtomic.tests.wallet.core.port.wallet.GetSingleWalletUseCase;
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
