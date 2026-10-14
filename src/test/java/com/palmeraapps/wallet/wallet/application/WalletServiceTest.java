package com.palmeraapps.wallet.wallet.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.palmeraapps.wallet.customer.domain.Customer;
import com.palmeraapps.wallet.customer.domain.CustomerNotFoundException;
import com.palmeraapps.wallet.customer.domain.CustomerRepository;

import com.palmeraapps.wallet.wallet.domain.Currency;
import com.palmeraapps.wallet.wallet.domain.Wallet;
import com.palmeraapps.wallet.wallet.domain.WalletAlreadyExistsException;
import com.palmeraapps.wallet.wallet.domain.WalletRepository;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {

    @Mock
    WalletRepository walletRepository;
    @Mock
    CustomerRepository customerRepository;

    UUID customerId = UUID.randomUUID();
    Currency currency = Currency.TRY;

    @Test
    void rejectsWhenCustomerNotFound() {
        // given
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        WalletService walletService = new WalletService(walletRepository, customerRepository);
        // when
        assertThatThrownBy(() -> walletService.openWallet(customerId, Currency.TRY))
                .isInstanceOf(CustomerNotFoundException.class);
        // then
        verify(walletRepository, never()).save(any(Wallet.class));
    }

    @Test
    void rejectsWhenWalletAlreadyExists() {
        // given
        Customer customer = new Customer("klay", "thompson", "klay@gmail.com");
        when(customerRepository.findById(customerId))
                .thenReturn(Optional.of(customer));
        when(walletRepository.existsByCustomerAndCurrency(customer, currency))
                .thenReturn(true);

        WalletService walletService = new WalletService(walletRepository, customerRepository);
        // when
        assertThatThrownBy(() -> walletService.openWallet(customerId, currency))
                .isInstanceOf(WalletAlreadyExistsException.class);
        // then
        verify(walletRepository, never()).save(any(Wallet.class));
    }

    @Test
    void opensNewWallet() {
        // given
        Customer customer = new Customer("klay", "thompson", "klay@gmail.com");
        when(customerRepository.findById(customerId))
                .thenReturn(Optional.of(customer));
        when(walletRepository.existsByCustomerAndCurrency(customer, currency))
                .thenReturn(false);
        // when
        WalletService walletService = new WalletService(walletRepository, customerRepository);

        walletService.openWallet(customerId, currency);

        //then

        verify(walletRepository, times(1)).save(any(Wallet.class));
    }

}
