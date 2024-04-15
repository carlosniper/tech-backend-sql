package com.playtomic.tests.wallet.adapter.outgoing.persistence.wallet;

import com.playtomic.tests.wallet.core.domain.Wallet;
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

    /*
    Optional<WalletEntity> walletEntityOptional = walletRepository.findById(wallet.getId());
        if(walletEntityOptional.isPresent()) {
            var entity = walletEntityOptional.get();
            entity.setAmount(wallet.getAmount());
            walletRepository.save(entity);
            log.info("Wallet updated!");
            return new Wallet(entity.getId(), entity.getAmount());
        }
        log.info("Wallet not found!!");
        return null;
     */
}
