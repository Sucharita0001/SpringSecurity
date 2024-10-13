package com.spring.security.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "customer")
@Data
@EqualsAndHashCode(exclude = "authority") // Exclude authorities to prevent infinite recursion
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "customer_id")
    private int id;
    @Column(name = "email_id")
    private String email;
    @Column(name = "user_password")
    private String password;
    @OneToOne(mappedBy = "customer", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Authority authority;
}
