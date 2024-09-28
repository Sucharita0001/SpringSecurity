package com.spring.security.configuration;

import com.spring.security.exceptionHandler.CustomAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfiguration {

    //For configuring which endpoint should be restricted or open
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                        (requests) ->
                                requests
                                        .requestMatchers("/restricted/**").authenticated()
                                        .requestMatchers("/open/**", "/actuator/**", "/h2-console/**", "/register").permitAll()
                        //.anyRequest().permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable).headers(header -> header.frameOptions(FrameOptionsConfig::disable));
        http.formLogin(withDefaults());
        http.httpBasic(
                httpBasicConfig ->
                        httpBasicConfig
                                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
        );
        http.exceptionHandling(
                httpSecurityExceptionHandlingConfig ->
                        httpSecurityExceptionHandlingConfig
                                .accessDeniedHandler(new CustomAccessDeniedHandler())
        );
        return http.build();
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
