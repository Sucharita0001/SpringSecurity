package com.spring.security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/restricted")
public class RestrictedController {

    @GetMapping("/admin")
    public ResponseEntity<String> restrictedEndpointForAdminOnly() {
        return new ResponseEntity<>("This endpoint is available for admin only", OK);
    }

    @GetMapping("/user")
    public ResponseEntity<String> restrictedEndpointForGeneralUserOnly() {
        return new ResponseEntity<>("This endpoint is available for general user only", OK);
    }

    @GetMapping
    public ResponseEntity<String> restrictedEndpoint() {
        return new ResponseEntity<>("This endpoint is restricted", OK);
    }
}
