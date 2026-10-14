package com.palmeraapps.wallet.customer.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.palmeraapps.wallet.customer.domain.Customer;
import com.palmeraapps.wallet.customer.domain.CustomerRepository;
import com.palmeraapps.wallet.customer.domain.EmailAlreadyRegisteredException;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public Customer registerCustomer(String firstName, String lastName, String email) {
        if (customerRepository.existsByEmail(email)) {
            throw new EmailAlreadyRegisteredException(email);
        }
        Customer customer = new Customer(firstName, lastName, email);
        return customerRepository.save(customer);
    }
}
