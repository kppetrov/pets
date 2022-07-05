package com.artplan.pets.service;

import com.artplan.pets.entity.User;

public interface UserService {
    User add(User user);
    Boolean existsByName(String name);
}
