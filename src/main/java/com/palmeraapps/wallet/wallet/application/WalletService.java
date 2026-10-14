package com.palmeraapps.wallet.wallet.application;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.palmeraapps.wallet.customer.domain.Customer;
import com.palmeraapps.wallet.customer.domain.CustomerNotFoundException;
import com.palmeraapps.wallet.customer.domain.CustomerRepository;
import com.palmeraapps.wallet.wallet.domain.Currency;
import com.palmeraapps.wallet.wallet.domain.Wallet;
import com.palmeraapps.wallet.wallet.domain.WalletAlreadyExistsException;
import com.palmeraapps.wallet.wallet.domain.WalletRepository;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final CustomerRepository customerRepository;

    public WalletService(WalletRepository walletRepository, CustomerRepository customerRepository) {
        this.walletRepository = walletRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public Wallet openWallet(UUID customerId, Currency currency) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        if (walletRepository.existsByCustomerAndCurrency(customer, currency)) {

            throw new WalletAlreadyExistsException(customerId, currency);
        }

        String walletNumber = String.valueOf(ThreadLocalRandom.current().nextLong(1_000_000_000_000_000L, 10_000_000_000_000_000L));

        Wallet wallet = new Wallet(customer,currency , walletNumber);
        return walletRepository.save(wallet);
    }

}
