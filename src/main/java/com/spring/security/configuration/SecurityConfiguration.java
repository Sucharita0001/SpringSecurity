package com.spring.security.configuration;

import com.spring.security.exceptionHandler.CustomAccessDeniedHandler;
import com.spring.security.exceptionHandler.CustomAuthenticationEntryPoint;
import com.spring.security.filter.EmailIdDomainValidationFilter;
import com.spring.security.filter.JwtTokenValidatorFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import static java.util.List.of;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl.fromHierarchy;
import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
public class SecurityConfiguration {

    //For configuring which endpoint should be restricted or open
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(STATELESS))
                .addFilterBefore(new EmailIdDomainValidationFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new JwtTokenValidatorFilter(), BasicAuthenticationFilter.class)
                .cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration configuration = new CorsConfiguration();
                        configuration.addAllowedHeader("*");
                        configuration.addAllowedMethod("*");
                        configuration.addAllowedOrigin("*");
                        configuration.setAllowCredentials(true);
                        configuration.setExposedHeaders(of(AUTHORIZATION));
                        configuration.setMaxAge(60L);
                        return configuration;
                    }
                })).authorizeHttpRequests(
                        (requests) ->
                                requests
                                        .requestMatchers("/open", "/actuator", "/h2-console/**", "/register", "/apiLogin").permitAll()
                                        /*.requestMatchers("/restricted/admin").hasAuthority("admin")
                                        .requestMatchers("/restricted").hasAnyAuthority("admin","general")*/ //implemented based on authorities
                                        .requestMatchers("/restricted/admin").hasRole("ADMIN")
                                        .requestMatchers("/restricted/user","/restricted").hasRole("USER")
                                        .requestMatchers("/user", "/userDetails","/update/**").authenticated()
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

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DefaultMethodSecurityExpressionHandler methodSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
        DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy);
        return expressionHandler;
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        return fromHierarchy("ROLE_OWNER > ROLE_ADMIN > ROLE_USER");
    }
}
