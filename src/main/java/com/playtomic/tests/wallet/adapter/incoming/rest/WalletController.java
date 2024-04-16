package com.playtomic.tests.wallet.adapter.incoming.rest;

import com.playtomic.tests.wallet.adapter.incoming.rest.request.ChargeWalletRequest;
import com.playtomic.tests.wallet.core.domain.model.Wallet;
import com.playtomic.tests.wallet.core.domain.port.incoming.wallet.ChargeWalletUseCase;
import com.playtomic.tests.wallet.core.domain.port.incoming.wallet.GetSingleWalletUseCase;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/wallet")
public class WalletController {

    private final GetSingleWalletUseCase getSingleWalletUseCase;
    private final ChargeWalletUseCase chargeWalletUseCase;

    @RequestMapping("/")
    void log() {
        log.info("Logging from /");
    }

    @GetMapping("/{walletId}")
    ResponseEntity<Wallet> getWallet(@PathVariable Long walletId) {
        log.info("Controller: Wallet id: {}", walletId);
        return new ResponseEntity<>(getSingleWalletUseCase.getWallet(walletId), HttpStatus.OK);
    }

    @PostMapping("/charge")
    ResponseEntity<Wallet> chargeWallet(@RequestBody @Valid ChargeWalletRequest request) {
        log.info("Controller: charge wallet: {}", request.walletId());
        return new ResponseEntity<>(chargeWalletUseCase.chargeWallet(request), HttpStatus.OK);

    }
}
