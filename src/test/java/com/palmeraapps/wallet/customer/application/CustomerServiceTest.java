package com.palmeraapps.wallet.customer.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.palmeraapps.wallet.customer.domain.Customer;
import com.palmeraapps.wallet.customer.domain.CustomerRepository;
import com.palmeraapps.wallet.customer.domain.EmailAlreadyRegisteredException;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Test
    void rejectsAlreadyRegisteredEmail() {
        // given: sahte repository "bu e-posta zaten kayıtlı" diyecek
        when(customerRepository.existsByEmail("john.doe@example.com")).thenReturn(true);
        CustomerService customerService = new CustomerService(customerRepository);

        // when + then: kayıt denemesi hata fırlatmalı
        assertThatThrownBy(() -> customerService.registerCustomer("John", "Doe", "john.doe@example.com"))
                .isInstanceOf(EmailAlreadyRegisteredException.class);

        // then: hiçbir şey kaydedilmemiş olmalı
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void registersNewCustomer() {
        // given: sahte repository "bu e-posta kayıtlı değil" diyecek
        when(customerRepository.existsByEmail("jane.smith@example.com")).thenReturn(false);
        CustomerService customerService = new CustomerService(customerRepository);

        // when
        customerService.registerCustomer("Jane", "Smith", "jane.smith@example.com");

        // then: müşteri bir kez kaydedilmiş olmalı
        verify(customerRepository, times(1)).save(any(Customer.class));
    }
}
