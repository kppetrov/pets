package com.artplan.pets.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.artplan.pets.dto.PetDto;
import com.artplan.pets.entity.Pet;
import com.artplan.pets.repository.PetRepository;
import com.artplan.pets.service.PetService;

@Service
@Transactional
public class PetServiceImpl implements PetService {
    private PetRepository petRepository;
    private ModelMapper modelMapper;

    @Autowired
    public void setPetRepository(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public List<PetDto> findAll() {
        return petRepository.findAll()
                .stream()
                .map(pet -> modelMapper.map(pet, PetDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public PetDto getById(Long id) {
        return modelMapper.map(petRepository.getReferenceById(id), PetDto.class);
    }

    @Override
    public PetDto add(PetDto pet) {
        Pet item = modelMapper.map(pet, Pet.class);
        item.setId(null);
        return modelMapper.map(petRepository.save(item), PetDto.class);
    }

    @Override
    public PetDto update(PetDto pet) {
        Pet item = modelMapper.map(pet, Pet.class); 
        return modelMapper.map(petRepository.save(item), PetDto.class);
    }

    @Override
    public void delete(Long id) {
        petRepository.deleteById(id);
    }

}
