package com.artplan.pets.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.artplan.pets.dto.ApiResponse;
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

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('pet:read_all')")
    public ResponseEntity<List<PetDtoResponse>> getAll() {
        return new ResponseEntity<>(petService.findAll(), HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('pet:read')")
    public ResponseEntity<List<PetDtoResponse>> getUserPets(Principal principal) {
        return new ResponseEntity<>(petService.findUserPets(principal.getName()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('pet:read')")
    public ResponseEntity<PetDtoResponse> getPet(@PathVariable Long id) {
        return new ResponseEntity<>(petService.getById(id), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('pet:write')")
    public ResponseEntity<PetDtoResponse> addPet(@RequestBody PetDtoRequest pet, Principal principal) {
        return new ResponseEntity<>(petService.add(pet, principal.getName()), HttpStatus.CREATED);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('pet:write')")
    public ResponseEntity<PetDtoResponse> updatePet(@RequestBody PetDtoRequest pet, Principal principal) {
        return new ResponseEntity<>(petService.update(pet, principal.getName()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('pet:write')")
    public ResponseEntity<ApiResponse> deletePet(@PathVariable Long id, Principal principal) {
        return new ResponseEntity<>(petService.delete(id, principal.getName()), HttpStatus.OK);
    }

}
