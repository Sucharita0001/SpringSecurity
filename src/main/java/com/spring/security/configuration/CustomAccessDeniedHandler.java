package com.spring.security.configuration;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.time.LocalDateTime;

import static jakarta.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static java.lang.String.format;
import static java.time.LocalDateTime.now;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setHeader("custom-denial-reason", "Authentication failed");
        response.setStatus(SC_FORBIDDEN);
        response.setContentType(APPLICATION_JSON_VALUE);
        String message = "{\"error\":\"This page is forbidden.\",\"message\":\"%s\",\"timestamp\":\"%s\"}";
        String formattedMessage = format(message, accessDeniedException.getMessage(), now());
        response.getWriter().write(formattedMessage);
    }
}
