package com.spring.security.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static jakarta.persistence.GenerationType.AUTO;

@Entity
@Data
@Table(name = "authorities")
@EqualsAndHashCode(exclude = "customer") // Exclude customer to prevent infinite recursion
public class Authority {

    @Id
    @GeneratedValue(strategy = AUTO)
    private long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
