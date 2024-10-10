package com.spring.security.configuration;

import com.spring.security.entity.Customer;
import com.spring.security.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import static java.util.List.of;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = this.customerRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not present with email " + username));
        ;
        return new User(
                username,
                customer.getPassword(),
                customer
                        .getAuthorities()
                        .stream()
                        .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                        .toList());
    }
}
