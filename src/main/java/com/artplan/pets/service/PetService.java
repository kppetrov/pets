package com.artplan.pets.service;

import java.util.List;

import com.artplan.pets.dto.PetDto;

public interface PetService {
    List<PetDto> findAll();
    PetDto getById(Long id);
    PetDto add(PetDto pet);
    PetDto update(PetDto pet);
    void delete(Long id);
}
