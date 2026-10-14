package com.palmeraapps.wallet.customer.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.palmeraapps.wallet.TestcontainersConfiguration;

@DataJpaTest
@Import(TestcontainersConfiguration.class)
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void findsCustomerByEmail() {
        // given: veritabanında bir müşteri var
        Customer customer = new Customer("John", "Doe", "john.doe@example.com");
        customerRepository.save(customer);

        // when: e-postasıyla arıyoruz
        Optional<Customer> found = customerRepository.findByEmail("john.doe@example.com");

        // then: müşteri bulunmalı ve bilgileri doğru olmalı
        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualTo("John");
    }
}
