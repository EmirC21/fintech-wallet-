package com.palmeraapps.wallet.wallet.domain;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import static org.assertj.core.api.Assertions.assertThat;

import com.palmeraapps.wallet.TestcontainersConfiguration;
import com.palmeraapps.wallet.customer.domain.Customer;
import com.palmeraapps.wallet.customer.domain.CustomerRepository;

@DataJpaTest
@Import(TestcontainersConfiguration.class)
class WalletRepositoryTest {

    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void findsAllWalletsOfCustomer() {
        // given
        Customer customer = new Customer("ali", "topac", "a@gmail.com");
        customerRepository.save(customer);
        Wallet wallet1 = new Wallet(customer, Currency.TRY, "TR123");
        Wallet wallet2 = new Wallet(customer, Currency.USD, "US123");
        walletRepository.save(wallet1);
        walletRepository.save(wallet2);
        // when
        List<Wallet> wallets = walletRepository.findByCustomer(customer);
        // then 
        assertThat(wallets).hasSize(2);


    }

}