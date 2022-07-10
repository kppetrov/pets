package com.artplan.pets.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.artplan.pets.entity.Gender;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetRequest {
    
    @NotNull(message = "{validation.pet.name.NotBlank.message}")
    @Size(min=3, max=25, message="{validation.pet.name.Size.message}")
    private String name;
    
    @NotNull(message = "{validation.typeId.name.NotBlank.message}")
    private Long typeId;
    
    @NotNull(message = "{validation.pet.gender.NotBlank.message}")
    private Gender gender;
    
    @DateTimeFormat(iso = ISO.DATE)
    @NotNull(message = "{validation.pet.birthdate.NotNull.message}")
    private LocalDate birthdate;
    
}
