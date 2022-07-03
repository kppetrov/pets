package com.artplan.pets.controller;

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

import com.artplan.pets.dto.PetDto;
import com.artplan.pets.service.PetService;

@RestController
@RequestMapping("/api")
public class PetRestController {

    private PetService petService;

    @Autowired
    public void setPetService(PetService petService) {
        this.petService = petService;
    }

    @GetMapping("/pets")
    @PreAuthorize("hasAuthority('pet:read')")
    public List<PetDto> getAll() {
        return petService.findAll();
    }

    @GetMapping("/pets/{id}")
    @PreAuthorize("hasAuthority('pet:read')")
    public PetDto getPet(@PathVariable Long id) {
        return petService.getById(id);
    }

    @PostMapping("/pets")
    @PreAuthorize("hasAuthority('pet:write')")
    public PetDto addPet(@RequestBody PetDto pet) {
        return petService.add(pet);
    }

    @PutMapping("/pets")
    @PreAuthorize("hasAuthority('pet:write')")
    public PetDto updatePet(@RequestBody PetDto item) {
        return petService.update(item);
    }

    @DeleteMapping("/pets/{id}")
    @PreAuthorize("hasAuthority('pet:write')")
    public void deletePet(@PathVariable Long id) {
        petService.delete(id);
    }

}
