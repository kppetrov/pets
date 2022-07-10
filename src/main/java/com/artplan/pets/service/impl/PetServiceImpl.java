package com.artplan.pets.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.artplan.pets.dto.ApiResponse;
import com.artplan.pets.dto.PetRequest;
import com.artplan.pets.dto.PetResponse;
import com.artplan.pets.entity.Pet;
import com.artplan.pets.entity.Type;
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

    @Override
    public List<PetResponse> findAll() {
        return petRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PetResponse getById(Long id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Pet not found with id: '%s'", id)));

        return toResponse(pet);
    }

    @Override
    public PetResponse add(PetRequest petRequest, String username) {
        User owner = userRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Owner not found with username: '%s'", username)));
        
        if (Boolean.TRUE.equals(petRepository.existsByNameAndOwner(petRequest.getName(), owner))) {
            throw new BadRequestException("Pet name is already taken");
        }
        
        Type type = typeRepository.findById(petRequest.getTypeId()).orElseThrow(() -> new ResourceNotFoundException(
                String.format("Type not found with id: '%s'", petRequest.getTypeId())));

        Pet pet = toEntity(petRequest);
        pet.setType(type);
        pet.setOwner(owner);
        return toResponse(petRepository.save(pet));
    }

    @Override
    public PetResponse update(PetRequest petRequest, Long id, String username) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Pet not found with id: '%s'", id)));

        Type type = typeRepository.findById(petRequest.getTypeId()).orElseThrow(() -> new ResourceNotFoundException(
                String.format("Type not found with id: '%s'", petRequest.getTypeId())));

        if (!pet.getOwner().getUsername().equals(username)) {
            throw new UnauthorizedException("Don't have permission to edit this pet");
        }

        if (Boolean.TRUE.equals(petRepository.existsByNameAndOwner(petRequest.getName(), pet.getOwner()))) {
            throw new BadRequestException("Name is already taken");
        }

        pet.setName(petRequest.getName());
        pet.setType(type);
        pet.setGender(petRequest.getGender());
        pet.setBirthdate(petRequest.getBirthdate());
        return toResponse(petRepository.save(pet));
    }

    @Override
    public ApiResponse delete(Long id, String username) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Pet not found with id: '%s'", id)));
        
        if (!pet.getOwner().getUsername().equals(username)) {
            throw new UnauthorizedException("Don't have permission to delete this pet");
        }
        
        petRepository.deleteById(id);
        return new ApiResponse(true, "You successfully deleted pet");
    }

    @Override
    public List<PetResponse> findUserPets(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Owner not found with username: '%s'", username)));
        
        return petRepository.findByOwner(user)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private Pet toEntity(PetRequest petRequest) {
        Pet pet =  new Pet();
        pet.setName(petRequest.getName());
        pet.setGender(petRequest.getGender());
        pet.setBirthdate(petRequest.getBirthdate());
        return pet;
    }
    
    private PetResponse toResponse(Pet pet) {
        PetResponse petResponse =  new PetResponse();
        petResponse.setId(pet.getId());
        petResponse.setName(pet.getName());
        petResponse.setType(pet.getType().getName());
        petResponse.setGender(pet.getGender().getValue());
        petResponse.setBirthdate(pet.getBirthdate());
        return petResponse;
    }
    
}
