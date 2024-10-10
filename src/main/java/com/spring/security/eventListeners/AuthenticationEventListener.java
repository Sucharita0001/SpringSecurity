package com.spring.security.eventListeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthenticationEventListener {

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent authenticationFailureEvent) {
        log.error("Login failed for user {} due to {}",
                authenticationFailureEvent.getAuthentication().getName(),
                authenticationFailureEvent.getException().getMessage()
        );
    }

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent authenticationSuccessEvent) {
        log.info("Login successful for user {}", authenticationSuccessEvent.getAuthentication().getName());
    }
}
