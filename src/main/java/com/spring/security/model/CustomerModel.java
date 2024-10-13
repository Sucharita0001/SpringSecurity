package com.spring.security.model;

import com.spring.security.entity.Authority;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.Set;

@Data
public class CustomerModel {
    private int id;
    private String email;
    private String authority;
}
