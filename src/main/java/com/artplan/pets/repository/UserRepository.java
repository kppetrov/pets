package com.artplan.pets.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.artplan.pets.entity.User;

public interface UserRepository  extends JpaRepository<User, Long>{
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
}
