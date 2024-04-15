package com.playtomic.tests.wallet.adapter.outgoing.persistence.wallet;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WalletRepository extends ListCrudRepository<WalletEntity, Long> {

    Optional<WalletEntity> findById(Long id);

    WalletEntity save(WalletEntity entity);
}
