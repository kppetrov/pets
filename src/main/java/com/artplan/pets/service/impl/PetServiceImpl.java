package com.artplan.pets.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.artplan.pets.dto.PetDtoRequest;
import com.artplan.pets.dto.PetDtoResponse;
import com.artplan.pets.entity.Pet;
import com.artplan.pets.entity.User;
import com.artplan.pets.repository.PetRepository;
import com.artplan.pets.repository.UserRepository;
import com.artplan.pets.service.PetService;

@Service
@Transactional
public class PetServiceImpl implements PetService {
    private PetRepository petRepository;
    private UserRepository userRepository;
    private ModelMapper modelMapper;

    @Autowired
    public void setPetRepository(PetRepository petRepository) {
        this.petRepository = petRepository;
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
        return modelMapper.map(petRepository.getReferenceById(id), PetDtoResponse.class);
    }

    @Override
    public PetDtoResponse add(PetDtoRequest pet, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("The user does not exist"));
        Pet item = modelMapper.map(pet, Pet.class);
        item.setId(null);
        item.setOwner(user);
        return modelMapper.map(petRepository.save(item), PetDtoResponse.class);
    }

    @Override
    public PetDtoResponse update(PetDtoRequest pet) {
        Pet item = modelMapper.map(pet, Pet.class); 
        return modelMapper.map(petRepository.save(item), PetDtoResponse.class);
    }

    @Override
    public void delete(Long id) {
        petRepository.deleteById(id);
    }

    @Override
    public List<PetDtoResponse> findUserPets(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("The user does not exist"));
        return petRepository.findByOwner(user)
                .stream()
                .map(pet -> modelMapper.map(pet, PetDtoResponse.class))
                .collect(Collectors.toList());
    }

}
