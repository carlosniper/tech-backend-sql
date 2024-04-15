package com.playtomic.tests.wallet.adapter.outgoing.persistence.wallet;

import com.playtomic.tests.wallet.core.domain.Wallet;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public abstract class WalletMapper {

    public abstract WalletEntity toWalletEntity(Wallet wallet);

    public abstract Wallet toWallet(WalletEntity walletEntity);
}
