package com.artplan.pets.controller;

import static com.artplan.pets.service.impl.TypeServiceImpl.NOT_FOUND;
import static com.artplan.pets.service.impl.TypeServiceImpl.DELETED_SUCCESSFULLY;
import static com.artplan.pets.service.impl.TypeServiceImpl.NAME_IS_ALREADY_TAKEN;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;

import com.artplan.pets.dto.ApiResponse;
import com.artplan.pets.dto.ExceptionResponse;
import com.artplan.pets.dto.TypeRequest;
import com.artplan.pets.dto.TypeResponse;
import com.fasterxml.jackson.databind.ObjectMapper;


@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails("admin")
@TestPropertySource("/application-test.properties")
@Sql(value = { "/insert-type-test-data.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class TypeRestControllerTest {
    
    final static String REST_URL = "/api/types";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAll_Should_ReturnFoundTypeEntries() throws Exception {
        
        TypeResponse typeResponse1 = new TypeResponse(101L, "Cat");
        TypeResponse typeResponse2 = new TypeResponse(102L, "Dog");
        String expectedResponseContent = objectMapper
                .writeValueAsString(new TypeResponse[] { typeResponse1, typeResponse2 });

        mockMvc.perform(get(REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResponseContent));

    }  
    
    @Test
    void getType_Should_ReturnHttpStatusCode404_When_TypeEntryNotFound() throws Exception {
        
        long id = 1000L;        
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.NOT_FOUND,
                String.format(NOT_FOUND, id));
        String expectedResponseContent = objectMapper.writeValueAsString(exceptionResponse);
 
        mockMvc.perform(get(REST_URL + "/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResponseContent)); 
 
    }
    
    @Test
    void getType_Should_ReturnFoundTypeEntry_When_TypeEntryFound() throws Exception {
        
        TypeResponse typeResponse = new TypeResponse(101L, "Cat");    
        String expectedResponseContent = objectMapper.writeValueAsString(typeResponse);
 
        mockMvc.perform(get(REST_URL + "/{id}", 101L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResponseContent));

    }
    
    @Test
    void addType_Should_AddTypeEntryAndReturnAddedEntry() throws Exception {

        TypeRequest typeRequest = new TypeRequest("Rabbit");
        String requestContent = objectMapper.writeValueAsString(typeRequest);
 
        mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestContent)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is(typeRequest.getName()))); 
 
    }
    
    @Test
    void addType_Should_ReturnHttpStatusCode400_When_NewTypeNameIsAlreadyTaken() throws Exception {

        TypeRequest typeRequest = new TypeRequest("Cat");
        String requestContent = objectMapper.writeValueAsString(typeRequest);
        
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.BAD_REQUEST,
                String.format(NAME_IS_ALREADY_TAKEN, typeRequest.getName()));
        String expectedResponseContent = objectMapper.writeValueAsString(exceptionResponse);
 
        mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestContent)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResponseContent)); 
    }
    
    @Test
    void updateType_Should_ReturnHttpStatusCode404_When_TypeEntryNotFound() throws Exception {
        long id = 1000L;
        TypeRequest typeRequest = new TypeRequest("Rabbit");
        String requestContent = objectMapper.writeValueAsString(typeRequest);
        
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.NOT_FOUND,
                String.format(NOT_FOUND, id));
        String expectedResponseContent = objectMapper.writeValueAsString(exceptionResponse);
 
        mockMvc.perform(put(REST_URL + "/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestContent)
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResponseContent)); 
 
    }
    
    @Test
    void updateType_Should_ReturnHttpStatusCode400_When_TypeNameIsAlreadyTaken() throws Exception {
        long id = 101L;
        TypeRequest typeRequest = new TypeRequest("Dog");
        String requestContent = objectMapper.writeValueAsString(typeRequest);
        
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.BAD_REQUEST,
                String.format(NAME_IS_ALREADY_TAKEN, typeRequest.getName()));
        String expectedResponseContent = objectMapper.writeValueAsString(exceptionResponse);
 
        mockMvc.perform(put(REST_URL + "/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestContent)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResponseContent)); 
    }
    
    @Test
    void updateType_Should_ReturnFoundUpdatedTypeEntry_When_TypeEntryFound() throws Exception {
        
        long id = 101L;
        TypeRequest typeRequest = new TypeRequest("Rabbit");
        TypeResponse typeResponse = new TypeResponse(id, "Rabbit");
        String requestContent = objectMapper.writeValueAsString(typeRequest);
        String expectedResponseContent = objectMapper.writeValueAsString(typeResponse);
 
        mockMvc.perform(put(REST_URL + "/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestContent)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResponseContent)); 

    }
    
    @Test
    void deleteType_Should_ReturnHttpStatusCode404_When_TypeEntryNotFound() throws Exception {
        
        long id = 1000L;        
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.NOT_FOUND,
                String.format(NOT_FOUND, id));
        String expectedResponseContent = objectMapper.writeValueAsString(exceptionResponse);
 
        mockMvc.perform(delete(REST_URL + "/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResponseContent)); 

    }
    
    @Test
    void deleteType_Should_DeletedFoundTypeEntry_When_TypeEntryFound() throws Exception {
        
        long id = 101L;        
        ApiResponse apiResponse = new ApiResponse( true,  DELETED_SUCCESSFULLY);
        String expectedResponseContent = objectMapper.writeValueAsString(apiResponse);
 
        mockMvc.perform(delete(REST_URL + "/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResponseContent)); 

    }

}
