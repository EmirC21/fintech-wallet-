package com.palmeraapps.wallet.wallet.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.palmeraapps.wallet.customer.domain.Customer;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {

    List<Wallet> findByCustomer(Customer customer);

    Optional<Wallet> findByWalletNumber(String walletNumber);

    boolean existsByCustomerAndCurrency(Customer customer, Currency currency);

}
