package com.artplan.pets.service.impl;

import static com.artplan.pets.utils.AppConstants.DEFOULT_ROLE;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.artplan.pets.dto.UserIdentityAvailability;
import com.artplan.pets.entity.User;
import com.artplan.pets.exception.BadRequestException;
import com.artplan.pets.repository.UserRepository;
import com.artplan.pets.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    
    public static final String USERNAME_IS_TAKEN_MSG = "Username is already taken: '%s'";
    
    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User addUserWithDefaultRole(User user) {
        if (Boolean.TRUE.equals(userRepository.existsByUsername(user.getUsername()))) {
            throw new BadRequestException(String.format(USERNAME_IS_TAKEN_MSG, user.getUsername()));
        }
        user.setRole(DEFOULT_ROLE);
        return userRepository.save(user);
    }

    @Override
    public UserIdentityAvailability checkUsernameAvailability(String username) {
        Boolean isAvailable = !userRepository.existsByUsername(username);
        return new UserIdentityAvailability(isAvailable);
    }

}
