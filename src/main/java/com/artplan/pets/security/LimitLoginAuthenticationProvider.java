package com.artplan.pets.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import com.artplan.pets.exception.FailAttemptsLimitIsOverException;
import com.artplan.pets.service.FailAttemptService;

public class LimitLoginAuthenticationProvider extends DaoAuthenticationProvider {

    private FailAttemptService failAttemptService;

    @Autowired
    public void setFailAttemptService(FailAttemptService failAttemptService) {
        this.failAttemptService = failAttemptService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        if (failAttemptService.failAttemptsLimitIsOver(authentication.getName())) {
            String msg = "The limit of failed attempts for the " + authentication.getName() + " has been exhausted";
            throw new FailAttemptsLimitIsOverException(msg);
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
