package com.artplan.pets.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

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
import com.artplan.pets.dto.PetRequest;
import com.artplan.pets.dto.PetResponse;
import com.artplan.pets.service.PetService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/pets")
@Slf4j
public class PetRestController {

    private PetService petService;

    @Autowired
    public void setPetService(PetService petService) {
        this.petService = petService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('pet:read_all')")
    public ResponseEntity<List<PetResponse>> getAll() {
        log.debug("get all pets");
        return new ResponseEntity<>(petService.findAll(), HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('pet:read')")
    public ResponseEntity<List<PetResponse>> getUserPets(Principal principal) {
        if (log.isDebugEnabled()) {
            log.info("get pets for user {}", principal.getName());   
        }
        return new ResponseEntity<>(petService.findUserPets(principal.getName()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('pet:read')")
    public ResponseEntity<PetResponse> getPet(@PathVariable Long id) {
        if (log.isDebugEnabled()) {
            log.info("get pet {}", id);   
        }
        return new ResponseEntity<>(petService.getById(id), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('pet:write')")
    public ResponseEntity<PetResponse> addPet(@Valid @RequestBody PetRequest pet, Principal principal) {
        if (log.isDebugEnabled()) {
            log.info("add pet {} for user {}", pet, principal.getName());   
        }
        return new ResponseEntity<>(petService.add(pet, principal.getName()), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('pet:write')")
    public ResponseEntity<PetResponse> updatePet(@Valid @RequestBody PetRequest pet, @PathVariable Long id,
            Principal principal) {
        if (log.isDebugEnabled()) {
            log.info("update pet {} {} for user {}", id, pet, principal.getName());   
        }
        return new ResponseEntity<>(petService.update(pet, id, principal.getName()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('pet:write')")
    public ResponseEntity<ApiResponse> deletePet(@PathVariable Long id, Principal principal) {
        if (log.isDebugEnabled()) {
            log.info("delete pet {} for user {}", id, principal.getName());   
        }
        return new ResponseEntity<>(petService.delete(id, principal.getName()), HttpStatus.OK);
    }

}
