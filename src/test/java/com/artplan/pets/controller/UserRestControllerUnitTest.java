package com.artplan.pets.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.artplan.pets.dto.UserIdentityAvailability;
import com.artplan.pets.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
class UserRestControllerUnitTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UserService service;
   
    @Test
    void Should_ReturnAvailableFalse_When_UsernameIsAlreadyTaken() throws Exception{
           when(service.checkUsernameAvailability("user")).thenReturn(new UserIdentityAvailability(false));
           mockMvc.perform(get("/api/users/checkUsernameAvailability?username=user"))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.available", is(Boolean.FALSE)));
    }
    
    @Test
    void Should_ReturnAvailableTrue_When_UsernameIsAvailable()  throws Exception{
        when(service.checkUsernameAvailability("user")).thenReturn(new UserIdentityAvailability(true));
        mockMvc.perform(get("/api/users/checkUsernameAvailability?username=user"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.available", is(Boolean.TRUE)));
    }

}
