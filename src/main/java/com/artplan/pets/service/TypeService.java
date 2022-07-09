package com.artplan.pets.service;

import java.util.List;

import com.artplan.pets.dto.ApiResponse;
import com.artplan.pets.dto.TypeRequest;
import com.artplan.pets.dto.TypeResponse;

public interface TypeService {
    List<TypeResponse> findAll();
    TypeResponse getById(Long id);
    TypeResponse add(TypeRequest typeRequest);
    TypeResponse update(TypeRequest typeRequest, Long id);
    ApiResponse delete(Long id);
}
