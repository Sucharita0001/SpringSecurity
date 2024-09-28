package com.spring.security.exceptionHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setHeader("custom-error-reason", "Authentication failed");
        response.setStatus(SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String message = "{\"error\":\"Not an authorized user\",\"message\":\"%s\",\"timestamp\":\"%s\"}";
        String formattedMessage = format(message, authException.getMessage(), now());
        response.getWriter().write(formattedMessage);
    }
}
