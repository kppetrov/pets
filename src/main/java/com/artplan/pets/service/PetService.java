package com.artplan.pets.service;

import java.util.List;

import com.artplan.pets.dto.ApiResponse;
import com.artplan.pets.dto.PetDtoRequest;
import com.artplan.pets.dto.PetDtoResponse;

public interface PetService {
    List<PetDtoResponse> findAll();
    PetDtoResponse getById(Long id);
    PetDtoResponse add(PetDtoRequest petDto, String username);
    PetDtoResponse update(PetDtoRequest petDto, String username);
    ApiResponse delete(Long id, String username);
    List<PetDtoResponse> findUserPets(String username);
}
