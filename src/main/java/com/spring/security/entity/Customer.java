package com.spring.security.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "customer")
@Data
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private int id;
    @Column(name = "email_id")
    private String email;
    @Column(name = "user_password")
    private String password;
    @Column(name = "user_role")
    private String role;
}
