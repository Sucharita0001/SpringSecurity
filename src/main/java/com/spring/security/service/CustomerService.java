package com.spring.security.service;

import com.spring.security.entity.Authority;
import com.spring.security.entity.Customer;
import com.spring.security.exception.CustomerAlreadyExistsException;
import com.spring.security.model.CustomerModel;
import com.spring.security.repository.AuthorityRepository;
import com.spring.security.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AuthorityRepository authorityRepository;

    public void saveCustomer(final Customer customer) throws CustomerAlreadyExistsException {
        Optional<Customer> customerRecord = this.customerRepository.findByEmail(customer.getEmail());
        if (customerRecord.isEmpty()) {
            Customer savedCustomer = customerRepository.save(customer);
            Authority authority = savedCustomer.getAuthority();
            authority.setCustomer(savedCustomer);
            authorityRepository.save(authority);
        } else {
            throw new CustomerAlreadyExistsException("Customer already exists with email " + customer.getEmail());
        }
    }

    public CustomerModel getCustomerDetails(final String email) {
        Optional<Customer> customer = this.customerRepository.findByEmail(email);
        if (customer.isPresent()) {
            CustomerModel customerModel = new CustomerModel();
            Customer cust = customer.get();
            customerModel.setId(cust.getId());
            customerModel.setEmail(cust.getEmail());
            customerModel.setAuthority(cust.getAuthority().getName());
            return customerModel;
        } else {
            return null;
        }
    }

    @PostFilter("filterObject.authorities.contains('ROLE_ADMIN')")
    public List<CustomerModel> getAdminUserDetails() {
        return new ArrayList<>(this.customerRepository.findAll().stream().map(customer -> {
            CustomerModel cust = new CustomerModel();
            cust.setId(customer.getId());
            cust.setEmail(customer.getEmail());
            cust.setAuthority(customer.getAuthority().getName());
            return cust;
        }).toList());
    }

    public String updateUserToAdmin(final String email) {
        Optional<Customer> customerRecord = this.customerRepository.findByEmail(email);
        if (customerRecord.isPresent()) {
            Customer customer = customerRecord.get();
            String role = customer.getAuthority().getName();
            if (role.equals("ROLE_ADMIN")) {
                throw new IllegalStateException("User already admin, can't upgrade");
            } else {
                Authority authority = new Authority();
                authority.setCustomer(customer);
                authority.setName(role);
                authorityRepository.save(authority);
                return "Customer role updated successfully";
            }
        } else {
            throw new UsernameNotFoundException("No user present with this email");
        }
    }
}
