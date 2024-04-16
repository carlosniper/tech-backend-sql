package com.playtomic.tests.wallet.adapter.outgoing.persistence.wallet;

import com.playtomic.tests.wallet.core.domain.model.Wallet;
import com.playtomic.tests.wallet.core.domain.port.outgoing.WalletPersistencePort;
import com.playtomic.tests.wallet.core.exceptions.wallet.WalletNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class WalletPersistenceAdapter implements WalletPersistencePort {

    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;

    @Override
    public Optional<Wallet> getWallet(Long id) {
        log.info("Finding wallet with id {}", id);
        return walletRepository.findById(id).map(entity -> walletMapper.toWallet(entity));
    }

    @Override
    public Wallet updateWallet(Wallet wallet) {
        log.info("updating wallet {} with new amount {}", wallet.getId(), wallet.getAmount());
        return walletRepository.findById(wallet.getId())
                .map(entity -> {
                    entity.setAmount(wallet.getAmount());
                    return walletRepository.save(entity);
                })
                .map(entity -> walletMapper.toWallet(entity))
                .orElseThrow(() -> new WalletNotFoundException());
    }
}
