package com.artplan.pets.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.artplan.pets.dto.PetDtoRequest;
import com.artplan.pets.dto.PetDtoResponse;
import com.artplan.pets.service.PetService;

@RestController
@RequestMapping("/api/pets")
public class PetRestController {

    private PetService petService;

    @Autowired
    public void setPetService(PetService petService) {
        this.petService = petService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('pet:read')")
    public List<PetDtoResponse> getAll() {
        return petService.findAll();
    }
    
    @GetMapping("/my")
    @PreAuthorize("hasAuthority('pet:read')")
    public List<PetDtoResponse> getAll(Principal principal) {
        return petService.findUserPets(principal.getName());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('pet:read')")
    public PetDtoResponse getPet(@PathVariable Long id) {
        return petService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('pet:write')")
    public PetDtoResponse addPet(@RequestBody PetDtoRequest pet, Principal principal) {
        return petService.add(pet, principal.getName());
    }

    @PutMapping
    @PreAuthorize("hasAuthority('pet:write')")
    public PetDtoResponse updatePet(@RequestBody PetDtoRequest pet) {
        return petService.update(pet);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('pet:write')")
    public void deletePet(@PathVariable Long id) {
        petService.delete(id);
    }

}
