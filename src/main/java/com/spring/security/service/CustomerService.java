package com.spring.security.service;

import com.spring.security.entity.Customer;
import com.spring.security.repository.AuthorityRepository;
import com.spring.security.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AuthorityRepository authorityRepository;

    public void saveCustomer(final Customer customer) {
        Customer savedCustomer = customerRepository.save(customer);

        // Step 2: Set the reference of the saved customer in each authority and save the authorities
        savedCustomer.getAuthorities().forEach(authority -> {
            authority.setCustomer(savedCustomer); // Link the saved customer to each authority
            authorityRepository.save(authority);  // Save the authority to the database
        });
    }
}
