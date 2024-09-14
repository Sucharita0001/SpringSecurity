package com.spring.security.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "additional.users")
@Getter
@Setter
public class ConfigProperties {
    private String userName;
    private String userPassword;
    private String userAuthority;
    private String adminName;
    private String adminPassword;
    private String adminAuthority;
}

