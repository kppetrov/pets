package com.artplan.pets.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.artplan.pets.entity.Pet;
import com.artplan.pets.entity.User;

public interface PetRepository extends JpaRepository<Pet, Long>{
    List<Pet> findByOwner(User owner);
    Boolean existsByNameAndOwner(String name, User owner);
}
