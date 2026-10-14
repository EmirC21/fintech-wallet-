package com.palmeraapps.wallet.wallet.domain;

import java.util.UUID;

public class WalletAlreadyExistsException extends RuntimeException {
    public WalletAlreadyExistsException(UUID customerId, Currency currency) {
        super("Customer " + customerId + " already has a " + currency + " wallet");
    }
}
