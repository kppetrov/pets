package com.artplan.pets.service;

import java.util.List;

import com.artplan.pets.dto.PetDtoRequest;
import com.artplan.pets.dto.PetDtoResponse;

public interface PetService {
    List<PetDtoResponse> findAll();
    PetDtoResponse getById(Long id);
    PetDtoResponse add(PetDtoRequest pet);
    PetDtoResponse update(PetDtoRequest pet);
    void delete(Long id);
}
