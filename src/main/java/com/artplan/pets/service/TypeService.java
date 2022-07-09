package com.artplan.pets.service;

import java.util.List;

import com.artplan.pets.dto.ApiResponse;
import com.artplan.pets.dto.TypeDto;

public interface TypeService {
    List<TypeDto> findAll();
    TypeDto getById(Long id);
    TypeDto add(TypeDto typeDto);
    TypeDto update(TypeDto typeDto);
    ApiResponse delete(Long id);
}
