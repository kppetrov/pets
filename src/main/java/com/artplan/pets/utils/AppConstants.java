package com.artplan.pets.utils;

import com.artplan.pets.entity.Role;

public class AppConstants {

    public static final long FAIL_ATTEMPT_LIMIT = 10;
    public static final long FAIL_ATTEMPT_PERIOD = 60; // minutes    
    public static final Role DEFOULT_ROLE = Role.USER;

    private AppConstants() {
        
    }
}
