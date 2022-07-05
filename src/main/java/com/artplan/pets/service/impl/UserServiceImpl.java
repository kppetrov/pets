package com.artplan.pets.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.artplan.pets.entity.Role;
import com.artplan.pets.entity.User;
import com.artplan.pets.repository.UserRepository;
import com.artplan.pets.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    @Override
    public User add(User user) {
        user.setId(null);
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }
        return userRepository.save(null);
    }

    @Override
    public Boolean existsByName(String name) {
        return userRepository.existsByName(name);
    }
    
}