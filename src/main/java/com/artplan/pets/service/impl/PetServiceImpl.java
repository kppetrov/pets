package com.artplan.pets.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.artplan.pets.dto.ApiResponse;
import com.artplan.pets.dto.PetDtoRequest;
import com.artplan.pets.dto.PetDtoResponse;
import com.artplan.pets.entity.Pet;
import com.artplan.pets.entity.User;
import com.artplan.pets.exception.BadRequestException;
import com.artplan.pets.exception.ResourceNotFoundException;
import com.artplan.pets.exception.UnauthorizedException;
import com.artplan.pets.repository.PetRepository;
import com.artplan.pets.repository.TypeRepository;
import com.artplan.pets.repository.UserRepository;
import com.artplan.pets.service.PetService;

@Service
@Transactional
public class PetServiceImpl implements PetService {
    private PetRepository petRepository;
    private TypeRepository typeRepository;
    private UserRepository userRepository;
    private ModelMapper modelMapper;

    @Autowired
    public void setPetRepository(PetRepository petRepository) {
        this.petRepository = petRepository;
    }
    
    @Autowired
    public void setTypeRepository(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public List<PetDtoResponse> findAll() {
        return petRepository.findAll()
                .stream()
                .map(pet -> modelMapper.map(pet, PetDtoResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public PetDtoResponse getById(Long id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Pet not found with id: '%s'", id)));
        
        return modelMapper.map(pet, PetDtoResponse.class);
    }

    @Override
    public PetDtoResponse add(PetDtoRequest petDto, String username) {
        User owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Owner not found with username: '%s'", username)));        
        if(Boolean.TRUE.equals(petRepository.existsByNameAndOwner(username, owner))) {
            throw new BadRequestException("Name is already taken");
        }         
        Pet pet = modelMapper.map(petDto, Pet.class);
        pet.setId(null);
        pet.setOwner(owner);
        return modelMapper.map(petRepository.save(pet), PetDtoResponse.class);
    }

    @Override
    public PetDtoResponse update(PetDtoRequest petDto, String username) {
        Pet pet = petRepository.findById(petDto.getId()).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Pet not found with id: '%s'", petDto.getId())));
        if(typeRepository.findById(petDto.getTypeId()).isEmpty()) {
            throw new ResourceNotFoundException(String.format("Type not found with id: '%s'", petDto.getTypeId())); 
        }
        if (!pet.getOwner().getUsername().equals(username)) {
            throw new UnauthorizedException("Don't have permission to edit this pet");
        }        
        if(Boolean.TRUE.equals(petRepository.existsByNameAndOwner(username, pet.getOwner()))) {
            throw new BadRequestException("Name is already taken");
        }
        Pet newPet = modelMapper.map(petDto, Pet.class);
        return modelMapper.map(petRepository.save(newPet), PetDtoResponse.class);
    }

    @Override
    public ApiResponse delete(Long id, String username) {
        Pet pet = petRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Pet not found with id: '%s'", id)));        
        if (!pet.getOwner().getUsername().equals(username)) {
            throw new UnauthorizedException("Don't have permission to delete this pet");
        }        
        petRepository.deleteById(id);
        return new ApiResponse(true, "You successfully deleted pet");
    }

    @Override
    public List<PetDtoResponse> findUserPets(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Owner not found with username: '%s'", username)));        
        return petRepository.findByOwner(user)
                .stream()
                .map(pet -> modelMapper.map(pet, PetDtoResponse.class))
                .collect(Collectors.toList());
    }

}
