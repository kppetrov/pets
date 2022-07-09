package com.artplan.pets.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.artplan.pets.dto.ApiResponse;
import com.artplan.pets.dto.TypeDto;
import com.artplan.pets.entity.Type;
import com.artplan.pets.exception.BadRequestException;
import com.artplan.pets.exception.ResourceNotFoundException;
import com.artplan.pets.repository.TypeRepository;
import com.artplan.pets.service.TypeService;

@Service
@Transactional
public class TypeServiceImpl implements TypeService {

    private TypeRepository typeRepository;
    private ModelMapper modelMapper;

    @Autowired
    public void setTypeRepository(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public List<TypeDto> findAll() {
        return typeRepository.findAll().stream().map(type -> modelMapper.map(type, TypeDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public TypeDto getById(Long id) {
        Type type = typeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Type not found with id: '%s'", id)));
        return modelMapper.map(type, TypeDto.class);
    }

    @Override
    public TypeDto add(TypeDto typeDto) {
        if (Boolean.TRUE.equals(typeRepository.existsByName(typeDto.getName()))) {
            throw new BadRequestException("Name is already taken");
        }
        Type type = modelMapper.map(typeDto, Type.class);
        type.setId(null);
        return modelMapper.map(typeRepository.save(type), TypeDto.class);
    }

    @Override
    public TypeDto update(TypeDto typeDto) {
        if (Boolean.TRUE.equals(typeRepository.existsByName(typeDto.getName()))) {
            throw new BadRequestException("Name is already taken");
        }
        Type type = modelMapper.map(typeDto, Type.class);
        return modelMapper.map(typeRepository.save(type), TypeDto.class);
    }

    @Override
    public ApiResponse delete(Long id) {
        if (Boolean.TRUE.equals(typeRepository.existsById(id))) {
            throw new ResourceNotFoundException(String.format("Type not found with id: '%s'", id));
        }
        typeRepository.deleteById(id);
        return new ApiResponse(true, "You successfully deleted pet");
    }

}
