package com.spring.security.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

import static jakarta.persistence.FetchType.EAGER;

@Entity
@Table(name = "customer")
@Data
@EqualsAndHashCode(exclude = "authorities") // Exclude authorities to prevent infinite recursion
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "customer_id")
    private int id;
    @Column(name = "email_id")
    private String email;
    @Column(name = "user_password")
    private String password;
    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Authority> authorities;
}
