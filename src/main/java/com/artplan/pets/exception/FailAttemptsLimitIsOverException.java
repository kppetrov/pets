package com.artplan.pets.exception;

import org.springframework.security.core.AuthenticationException;

public class FailAttemptsLimitIsOverException extends AuthenticationException{
    private static final long serialVersionUID = 1961474492744127565L;

    public FailAttemptsLimitIsOverException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public FailAttemptsLimitIsOverException(String msg) {
        super(msg);
    }

}
