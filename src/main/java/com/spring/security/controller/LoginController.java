package com.spring.security.controller;

import com.spring.security.entity.Customer;
import com.spring.security.repository.CustomerRepository;
import com.spring.security.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
public class LoginController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<String> registerUser(@RequestBody Customer customer) {
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        this.customerService.saveCustomer(customer);
        return new ResponseEntity<>("Customer created for user " + customer.getEmail(), HttpStatus.CREATED);
    }
}