package com.artplan.pets.service.impl;

import static com.artplan.pets.utils.AppConstants.DEFAULT_ROLE;

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

    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User add(User user) {
        if (Boolean.TRUE.equals(userRepository.existsByUsername(user.getUsername()))) {
            throw new BadRequestException("Username is already taken");
        }

        user.setId(null);
        if (user.getRole() == null) {
            user.setRole(DEFAULT_ROLE);
        }
        return userRepository.save(user);
    }

    @Override
    public UserIdentityAvailability checkUsernameAvailability(String username) {
        Boolean isAvailable = !userRepository.existsByUsername(username);
        return new UserIdentityAvailability(isAvailable);
    }

}
