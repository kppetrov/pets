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

import com.artplan.pets.dto.TypeDto;
import com.artplan.pets.service.TypeService;

@RestController
@RequestMapping("/api/types")
public class TypeRestController {

    private TypeService typeService;

    @Autowired
    public void setTypeService(TypeService petService) {
        this.typeService = petService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('type:read')")
    public List<TypeDto> getAll() {
        return typeService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('type:read')")
    public TypeDto getType(@PathVariable Long id) {
        return typeService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('type:write')")
    public TypeDto addType(@RequestBody TypeDto typeDto) {        

        return typeService.add(typeDto);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('type:write')")
    public TypeDto updateType(@RequestBody TypeDto typeDto) {
        return typeService.update(typeDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('type:write')")
    public void deleteType(@PathVariable Long id) {
        typeService.delete(id);
    }
}
