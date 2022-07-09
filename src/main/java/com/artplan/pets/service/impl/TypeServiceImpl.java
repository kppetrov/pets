package com.artplan.pets.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.artplan.pets.dto.ApiResponse;
import com.artplan.pets.dto.TypeRequest;
import com.artplan.pets.dto.TypeResponse;
import com.artplan.pets.entity.Type;
import com.artplan.pets.exception.BadRequestException;
import com.artplan.pets.exception.ResourceNotFoundException;
import com.artplan.pets.repository.TypeRepository;
import com.artplan.pets.service.TypeService;

@Service
@Transactional
public class TypeServiceImpl implements TypeService {

    private TypeRepository typeRepository;

    @Autowired
    public void setTypeRepository(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    @Override
    public List<TypeResponse> findAll() {
        return typeRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TypeResponse getById(Long id) {
        Type type = typeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Type not found with id: '%s'", id)));
        return toResponse(type);
    }

    @Override
    public TypeResponse add(TypeRequest typeRequest) {
        if (Boolean.TRUE.equals(typeRepository.existsByName(typeRequest.getName()))) {
            throw new BadRequestException("Name is already taken");
        }
        Type type = toEntity(typeRequest);
        return toResponse(typeRepository.save(type));
    }

    @Override
    public TypeResponse update(TypeRequest typeRequest, Long id) {
        if (Boolean.TRUE.equals(typeRepository.existsByName(typeRequest.getName()))) {
            throw new BadRequestException("Name is already taken");
        }
        Type type = typeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Type not found with id: '%s'", id)));        
        type.setName(typeRequest.getName());
        return toResponse(typeRepository.save(type));
    }

    @Override
    public ApiResponse delete(Long id) {
        if (Boolean.TRUE.equals(typeRepository.existsById(id))) {
            throw new ResourceNotFoundException(String.format("Type not found with id: '%s'", id));
        }
        typeRepository.deleteById(id);
        return new ApiResponse(true, "You successfully deleted pet");
    }
    
    private Type toEntity(TypeRequest typeRequest) {
        Type type =  new Type();
        type.setName(typeRequest.getName());
        return type;
    }
    
    private TypeResponse toResponse(Type type) {
        TypeResponse typeResponse =  new TypeResponse();
        typeResponse.setId(type.getId());
        typeResponse.setName(type.getName());
        return typeResponse;
    }

}
