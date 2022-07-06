package com.artplan.pets.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.artplan.pets.dto.TypeDto;
import com.artplan.pets.entity.Type;
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
        return typeRepository.findAll()
                .stream()
                .map(type -> modelMapper.map(type, TypeDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public TypeDto getById(Long id) {
        return modelMapper.map(typeRepository.getReferenceById(id), TypeDto.class);
    }

    @Override
    public TypeDto add(TypeDto typeDto) {
        Type type = modelMapper.map(typeDto, Type.class);

        type.setId(null);

        return modelMapper.map(typeRepository.save(type), TypeDto.class);
    }

    @Override
    public TypeDto update(TypeDto typeDto) {
        Type type = modelMapper.map(typeDto, Type.class);

        return modelMapper.map(typeRepository.save(type), TypeDto.class);
    }

    @Override
    public void delete(Long id) {
        typeRepository.deleteById(id);
    }

}
