package com.artplan.pets.service;

import java.util.List;

import com.artplan.pets.dto.ApiResponse;
import com.artplan.pets.dto.PetRequest;
import com.artplan.pets.dto.PetResponse;

public interface PetService {
    List<PetResponse> findAll();
    PetResponse getById(Long id);
    PetResponse add(PetRequest petRequest, String username);
    PetResponse update(PetRequest petRequest, Long id, String username);
    ApiResponse delete(Long id, String username);
    List<PetResponse> findUserPets(String username);
}
