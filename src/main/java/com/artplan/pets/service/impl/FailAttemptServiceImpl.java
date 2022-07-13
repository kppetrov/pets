package com.artplan.pets.service.impl;

import static com.artplan.pets.utils.AppConstants.FAIL_ATTEMPT_LIMIT;
import static com.artplan.pets.utils.AppConstants.FAIL_ATTEMPT_PERIOD;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.artplan.pets.entity.FailAttempt;
import com.artplan.pets.repository.FailAttemptRepository;
import com.artplan.pets.service.FailAttemptService;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class FailAttemptServiceImpl implements FailAttemptService {
    
    private FailAttemptRepository failAttemptRepository;
    
    @Autowired
    public void setFailAttemptRepository(FailAttemptRepository failAttemptRepository) {
        this.failAttemptRepository = failAttemptRepository;
    }

    @Override
    public void addFailAttempts(String username) {
        if (log.isDebugEnabled()) {
            log.debug("add fail attempt for {}", username);   
        }
        failAttemptRepository.save(new FailAttempt(null, username, LocalDateTime.now()));
    }

    @Override
    public void resetFailAttempts(String username) {
        if (log.isDebugEnabled()) {
            log.debug("reset fail attempt for {}", username);   
        }
        failAttemptRepository.deleteByUsername(username);
    }
    
    @Override
    public boolean failAttemptsLimitIsOver(String username) {
        if (log.isDebugEnabled()) {
            log.debug("check fail attempt for {}", username);   
        }
        LocalDateTime attemptTime = LocalDateTime.now().minusMinutes(FAIL_ATTEMPT_PERIOD);
        Long attemptsCount = failAttemptRepository.countByUsernameAndAttemptTimeGreaterThan(username, attemptTime);
        return attemptsCount >= FAIL_ATTEMPT_LIMIT;
    }    

}
