package com.artplan.pets.service;

public interface FailAttemptService {
    void addFailAttempts(String username);
    void resetFailAttempts(String username);
    boolean failAttemptsLimitIsOver(String username);
}
