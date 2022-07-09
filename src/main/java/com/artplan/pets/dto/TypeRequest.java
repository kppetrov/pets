package com.artplan.pets.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypeRequest {
    @NotNull(message = "{validation.type.name.NotBlank.message}")
    @Size(min=3, max=25, message="{validation.type.name.Size.message}")
    private String name;
}
