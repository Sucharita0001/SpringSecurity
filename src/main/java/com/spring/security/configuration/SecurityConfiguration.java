package com.spring.security.configuration;

import com.spring.security.configuration.properties.ConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.core.userdetails.User.withUsername;

@Configuration
public class SecurityConfiguration {

    @Autowired
    private ConfigProperties configProperties;

    //For configuring which endpoint should be restricted or open
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                (requests) ->
                        requests
                                .requestMatchers("/restricted/**").authenticated()
                                .requestMatchers("/open/**","/actuator/**").permitAll()
        );
        http.formLogin(withDefaults());
        /*http.formLogin(AbstractHttpConfigurer::disable);*/ //for disabling form login
        http.httpBasic(withDefaults());
        return http.build();
    }

    //Using in memory user details
    @Bean
    UserDetailsService userDetailsService() {
        UserDetails user = withUsername(configProperties.getUserName())
                .password(configProperties.getUserPassword())
                .authorities(configProperties.getUserAuthority())
                .build();
        UserDetails admin = withUsername(configProperties.getAdminName())
                .password(configProperties.getAdminPassword())
                .authorities(configProperties.getAdminAuthority())
                .build();
        return new InMemoryUserDetailsManager(user, admin);
    }

    //By default, will consider BcryptEncoder
    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // Check password is compromised or not
    @Bean
    CompromisedPasswordChecker compromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }
}
