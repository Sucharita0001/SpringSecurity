package com.spring.security.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static java.util.Base64.getDecoder;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

public class EmailIdDomainValidationFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var req = (HttpServletRequest) request;
        var res = (HttpServletResponse) response;
        String header = req.getHeader(AUTHORIZATION);
        if (header != null) { //Checking is authorization header present at all
            header = header.trim();
            if (header.startsWith("Basic ")) { //If basic authorization is used or not
                try {
                    String token = new String(getDecoder().decode(header.substring(6).getBytes(StandardCharsets.UTF_8))); //Getting decoded token, must be like username:password format
                    int delim = token.indexOf(":");
                    if (delim == -1) { //if : is not present s token is invalid
                        throw new BadCredentialsException("Invalid basic authentication token");
                    }
                    if (token.substring(0, delim).toLowerCase().contains("test")) { //blocking users having test inside their email id
                        res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        return;
                    }
                } catch (IllegalArgumentException e) {
                    throw new BadCredentialsException("Failed to decode authentication token"); // if unable to decode token
                }
            }
        }
        chain.doFilter(req, res);
    }
}
