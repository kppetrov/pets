package com.artplan.pets.controller;

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
import com.artplan.pets.dto.TypeRequest;
import com.artplan.pets.dto.TypeResponse;
import com.artplan.pets.service.TypeService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/types")
@Slf4j
public class TypeRestController {

    private TypeService typeService;

    @Autowired
    public void setTypeService(TypeService petService) {
        this.typeService = petService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('type:read')")
    public ResponseEntity<List<TypeResponse>> getAll() {
        log.debug("get all types");
        return new ResponseEntity<>(typeService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('type:read')")
    public ResponseEntity<TypeResponse> getType(@Valid @PathVariable Long id) {
        if (log.isDebugEnabled()) {
            log.info("get type {}", id);   
        }
        return new ResponseEntity<>(typeService.getById(id), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('type:write')")
    public ResponseEntity<TypeResponse> addType(@Valid @RequestBody TypeRequest typeRequest) {
        if (log.isDebugEnabled()) {
            log.info("add type {}", typeRequest);   
        }
        return new ResponseEntity<>(typeService.add(typeRequest), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('type:write')")
    public ResponseEntity<TypeResponse> updateType(@Valid @RequestBody TypeRequest typeRequest, @PathVariable Long id) {
        if (log.isDebugEnabled()) {
            log.info("update type {} {} ", id, typeRequest);   
        }
        return new ResponseEntity<>(typeService.update(typeRequest, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('type:write')")
    public ResponseEntity<ApiResponse> deleteType(@PathVariable Long id) {
        if (log.isDebugEnabled()) {
            log.info("delete type {}", id);   
        }
        return new ResponseEntity<>(typeService.delete(id), HttpStatus.OK);
    }
}
