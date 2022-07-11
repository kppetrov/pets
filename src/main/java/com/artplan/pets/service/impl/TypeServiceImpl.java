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
    
    public static final String NOT_FOUND = "Type not found with id: '%s'"; 
    public static final String NAME_IS_ALREADY_TAKEN = "Type name is already taken: '%s'"; 
    public static final String DELETED_SUCCESSFULLY = "You successfully deleted type"; 

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
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND, id)));
        return toResponse(type);
    }

    @Override
    public TypeResponse add(TypeRequest typeRequest) {
        if (Boolean.TRUE.equals(typeRepository.existsByName(typeRequest.getName()))) {
            throw new BadRequestException(String.format(NAME_IS_ALREADY_TAKEN, typeRequest.getName()));
        }
        Type type = toEntity(typeRequest);
        return toResponse(typeRepository.save(type));
    }

    @Override
    public TypeResponse update(TypeRequest typeRequest, Long id) {
        if (Boolean.TRUE.equals(typeRepository.existsByName(typeRequest.getName()))) {
            throw new BadRequestException(String.format(NAME_IS_ALREADY_TAKEN, typeRequest.getName()));
        }
        Type type = typeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(NOT_FOUND, id)));        
        type.setName(typeRequest.getName());
        return toResponse(typeRepository.save(type));
    }

    @Override
    public ApiResponse delete(Long id) {
        if (typeRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException(String.format(NOT_FOUND, id));
        }
        typeRepository.deleteById(id);
        return new ApiResponse(true, DELETED_SUCCESSFULLY);
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
