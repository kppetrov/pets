package com.artplan.pets.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.artplan.pets.exception.FailAttemptsLimitIsOverException;
import com.artplan.pets.service.FailAttemptService;

public class LimitLoginAuthenticationProvider extends DaoAuthenticationProvider {
    
    public static final String LIMIT_IS_OWER_MSG = "The limit of failed attempts for the '%s' has been exhausted";

    private FailAttemptService failAttemptService;

    @Autowired
    public void setFailAttemptService(FailAttemptService failAttemptService) {
        this.failAttemptService = failAttemptService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        if (failAttemptService.failAttemptsLimitIsOver(authentication.getName())) {
            throw new FailAttemptsLimitIsOverException(String.format(LIMIT_IS_OWER_MSG, authentication.getName()));
        }

        try {
            Authentication auth = super.authenticate(authentication);
            failAttemptService.resetFailAttempts(authentication.getName());
            return auth;
        } catch (AuthenticationException e) {
            failAttemptService.addFailAttempts(authentication.getName());
            throw e;
        }
    }

}
