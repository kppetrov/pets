package com.artplan.pets.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    
    @NotNull(message = "{validation.login.username.NotBlank.message}")
    @Size(min=3, max=25, message="{validation.login.username.Size.message}")
    private String username;
    
    @NotNull(message = "{validation.login.password.NotBlank.message}")
    @Size(min=3, max=250, message="{validation.login.password.Size.message}")
    private String password;
    
}
