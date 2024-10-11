package com.spring.security.service;

import com.spring.security.entity.Authority;
import com.spring.security.entity.Customer;
import com.spring.security.model.CustomerModel;
import com.spring.security.repository.AuthorityRepository;
import com.spring.security.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

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

    public CustomerModel getCustomerDetails(final String email) {
        Optional<Customer> customer = this.customerRepository.findByEmail(email);
        if (customer.isPresent()) {
            CustomerModel customerModel = new CustomerModel();
            Customer cust = customer.get();
            customerModel.setId(cust.getId());
            customerModel.setEmail(cust.getEmail());
            customerModel.setAuthorities(cust.getAuthorities().stream().map(Authority::getName).collect(toSet()));
            return customerModel;
        } else {
            return null;
        }
    }
}
