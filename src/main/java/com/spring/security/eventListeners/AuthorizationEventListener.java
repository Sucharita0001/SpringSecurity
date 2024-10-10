package com.spring.security.eventListeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authorization.event.AuthorizationDeniedEvent;
import org.springframework.security.authorization.event.AuthorizationGrantedEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthorizationEventListener {

    @EventListener
    public void onFailure(AuthorizationDeniedEvent authorizationDeniedEvent) {
        log.error("Authorization failed for user {} due to {}",
                authorizationDeniedEvent.getAuthentication().get().getName(),
                authorizationDeniedEvent.getAuthorizationDecision().toString()
        );
    }
}
