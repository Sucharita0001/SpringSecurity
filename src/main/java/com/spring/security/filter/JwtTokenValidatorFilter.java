package com.spring.security.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.spring.security.constant.Constants.*;
import static io.jsonwebtoken.security.Keys.hmacShaKeyFor;
import static jakarta.servlet.http.HttpServletResponse.SC_REQUEST_TIMEOUT;
import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.security.core.authority.AuthorityUtils.commaSeparatedStringToAuthorityList;

@Slf4j
public class JwtTokenValidatorFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = request.getHeader(AUTHORIZATION);
        if (null != jwtToken) {
            try {
                Claims claims = Jwts.parser()
                        .verifyWith(hmacShaKeyFor(JWT_SECRET.getBytes(UTF_8)))
                        .build()
                        .parseSignedClaims(jwtToken)
                        .getPayload();
                String username = String.valueOf(claims.get(USERNAME));
                String authorities = String.valueOf(claims.get(AUTHORITIES));
                Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, commaSeparatedStringToAuthorityList(authorities));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (JwtException exp) {
                log.error("Validation failed due to {}", exp.getMessage());
                response.setStatus(SC_REQUEST_TIMEOUT);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                String message = "{\"error\":\"%s\",\"timestamp\":\"%s\"}";
                String formattedMessage = format(message, exp.getMessage(), now());
                response.getWriter().write(formattedMessage);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().equals("/apiLogin");
    }
}
