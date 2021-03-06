package com.artplan.pets.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PetResponse {
    private Long id;
    private String name;
    private String type;
    private String gender;
    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate birthdate;
}
