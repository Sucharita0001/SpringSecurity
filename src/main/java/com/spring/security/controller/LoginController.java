package com.spring.security.controller;

import com.spring.security.entity.Customer;
import com.spring.security.model.CustomerModel;
import com.spring.security.model.LoginRequestDTO;
import com.spring.security.model.LoginResponseDTO;
import com.spring.security.service.CustomerService;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.stream.Collectors;

import static com.spring.security.constant.Constants.*;
import static io.jsonwebtoken.security.Keys.hmacShaKeyFor;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.joining;

@RestController
@RequestMapping
public class LoginController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping(("/register"))
    public ResponseEntity<String> registerUser(@RequestBody Customer customer) {
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        this.customerService.saveCustomer(customer);
        return new ResponseEntity<>("Customer created for user " + customer.getEmail(), HttpStatus.CREATED);
    }

    @RequestMapping("/user")
    public ResponseEntity<CustomerModel> getUserDetailsAfterLogin(Authentication authentication) {
        return new ResponseEntity<>(this.customerService.getCustomerDetails(authentication.getName()), HttpStatus.OK);
    }

    @PostMapping("/apiLogin")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(
                loginRequestDTO.email(),
                loginRequestDTO.password()
        );
        Authentication authenticate = authenticationManager.authenticate(authentication);
        String jwt = (null != authenticate && authenticate.isAuthenticated()) ? Jwts.builder()
                .issuer("Spring Security Application")
                .claim(USERNAME, authenticate.getName())
                .claim(AUTHORITIES,
                        authenticate
                                .getAuthorities()
                                .stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(joining(","))
                )
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + 300000)) //setting expiration time to 300s after generation of token
                .signWith(hmacShaKeyFor(JWT_SECRET.getBytes(UTF_8)))
                .compact()
                : null;
        return new ResponseEntity<>(new LoginResponseDTO(HttpStatus.CREATED.name(), jwt), HttpStatus.CREATED);
    }
}