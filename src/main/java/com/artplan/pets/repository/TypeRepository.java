package com.artplan.pets.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.artplan.pets.entity.Type;

public interface TypeRepository extends JpaRepository<Type, Long>{
    Boolean existsByName(String name);
}
