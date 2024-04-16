package com.playtomic.tests.wallet.adapter.outgoing.persistence.wallet;

import com.playtomic.tests.wallet.core.domain.model.Wallet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class WalletMapper {

    public abstract WalletEntity toWalletEntity(Wallet wallet);

    public abstract Wallet toWallet(WalletEntity walletEntity);
}
