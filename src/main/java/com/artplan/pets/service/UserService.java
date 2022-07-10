package com.artplan.pets.service;

import com.artplan.pets.dto.UserIdentityAvailability;
import com.artplan.pets.entity.User;

public interface UserService {
    User addUserWithDefaultRole(User user);
    UserIdentityAvailability checkUsernameAvailability(String name);
}
