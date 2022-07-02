package com.artplan.pets.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.artplan.pets.entity.Pet;

public interface PetRepository extends JpaRepository<Pet, Long>{

}
