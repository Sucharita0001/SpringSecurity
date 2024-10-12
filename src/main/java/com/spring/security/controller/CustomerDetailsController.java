package com.spring.security.controller;

import com.spring.security.model.CustomerModel;
import com.spring.security.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.PARTIAL_CONTENT;

@RestController
public class CustomerDetailsController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/userDetails")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CustomerModel>> getAllAdminUserDetails(){
        return new ResponseEntity<>(this.customerService.getAdminUserDetails(), PARTIAL_CONTENT);
    }
}
