package com.spring.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/open")
public class OpenController {

    @GetMapping
    public ResponseEntity<String> openEndpoint() {
        return new ResponseEntity<>("This is a open for all endpoint", OK);
    }
}
