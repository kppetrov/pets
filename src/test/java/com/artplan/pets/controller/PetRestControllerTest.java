package com.artplan.pets.controller;

import static com.artplan.pets.service.impl.PetServiceImpl.NAME_IS_ALREADY_TAKEN;
import static com.artplan.pets.service.impl.PetServiceImpl.PET_NOT_FOUND;
import static com.artplan.pets.service.impl.PetServiceImpl.TYPE_NOT_FOUND;
import static com.artplan.pets.service.impl.PetServiceImpl.DELETED_SUCCESSFULLY;
import static com.artplan.pets.service.impl.PetServiceImpl.DONT_HAVE_PERMISSION;
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

import java.time.LocalDate;

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
import com.artplan.pets.dto.PetRequest;
import com.artplan.pets.dto.PetResponse;
import com.artplan.pets.entity.Gender;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = { "/insert-pet-test-data.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class PetRestControllerTest {
    
    final static String REST_URL = "/api/pets";
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithUserDetails("user")
    void getAll_Should_ReturnFoundUserPetEntries() throws Exception {
        
        PetResponse petResponse1 = new PetResponse(101L, "Murka", "Cat", "FEMAIL", LocalDate.of(2020, 5, 20));
        PetResponse petResponse2 = new PetResponse(102L, "Bobik", "Dog", "MAIL", LocalDate.of(2019, 4, 10));
        String expectedResponseContent = objectMapper
                .writeValueAsString(new PetResponse[] { petResponse1, petResponse2 });

        mockMvc.perform(get(REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResponseContent));

    } 
    
    @Test
    @WithUserDetails("admin")
    void getAll_Should_ReturnFoundAllPetEntries() throws Exception {
        
        PetResponse petResponse1 = new PetResponse(101L, "Murka", "Cat", "FEMAIL", LocalDate.of(2020, 5, 20));
        PetResponse petResponse2 = new PetResponse(102L, "Bobik", "Dog", "MAIL", LocalDate.of(2019, 4, 10));
        PetResponse petResponse3 = new PetResponse(103L, "Roger", "Rabbit", "MAIL", LocalDate.of(1988, 6, 21));
        String expectedResponseContent = objectMapper
                .writeValueAsString(new PetResponse[] { petResponse1, petResponse2, petResponse3 });

        mockMvc.perform(get(REST_URL + "/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResponseContent));

    } 
    
    @Test
    @WithUserDetails("user")
    void getPet_Should_ReturnHttpStatusCode404_When_PetEntryNotFound() throws Exception {
        
        long id = 1000L;        
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.NOT_FOUND,
                String.format(PET_NOT_FOUND, id));
        String expectedResponseContent = objectMapper.writeValueAsString(exceptionResponse);
 
        mockMvc.perform(get(REST_URL + "/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResponseContent)); 
 
    }
    
    @Test
    @WithUserDetails("user")
    void getPet_Should_ReturnFoundPetEntry_When_PetEntryFound() throws Exception {
        
        PetResponse petResponse = new PetResponse(101L, "Murka", "Cat", "FEMAIL", LocalDate.of(2020, 5, 20));    
        String expectedResponseContent = objectMapper.writeValueAsString(petResponse);
 
        mockMvc.perform(get(REST_URL + "/{id}", 101L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResponseContent));

    }
    
    @Test
    @WithUserDetails("user")
    void addPet_Should_AddPetEntryAndReturnAddedEntry() throws Exception {

        PetRequest petRequest = new PetRequest("Fiska", 101L, Gender.FEMAIL, LocalDate.of(2022, 7, 12)); 
        String requestContent = objectMapper.writeValueAsString(petRequest);
 
        mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestContent)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is(petRequest.getName())))
                .andExpect(jsonPath("$.type", is("Cat")))
                .andExpect(jsonPath("$.gender", is("FEMAIL")))
                .andExpect(jsonPath("$.birthdate", is("2022-07-12"))); 
 
    }
    
    @Test
    @WithUserDetails("user")
    void addPet_Should_ReturnHttpStatusCode400_When_NewPetNameIsAlreadyTaken() throws Exception {

        PetRequest petRequest = new PetRequest("Murka", 101L, Gender.FEMAIL, LocalDate.of(2022, 7, 12)); 
        String requestContent = objectMapper.writeValueAsString(petRequest);
        
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.BAD_REQUEST,
                String.format(NAME_IS_ALREADY_TAKEN, petRequest.getName()));
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
    @WithUserDetails("user")
    void addPet_Should_ReturnHttpStatusCode400_When_NewPetTypeNotFound() throws Exception {
        long typeId = 1000L;
        
        PetRequest petRequest = new PetRequest("Fiska", typeId, Gender.FEMAIL, LocalDate.of(2022, 7, 12)); 
        String requestContent = objectMapper.writeValueAsString(petRequest);
        
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.BAD_REQUEST,
                String.format(TYPE_NOT_FOUND, typeId));
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
    @WithUserDetails("user")
    void updatePet_Should_ReturnHttpStatusCode404_When_PetEntryNotFound() throws Exception {
        long id = 1000L;
        PetRequest petRequest = new PetRequest("Fiska", 101L, Gender.FEMAIL, LocalDate.of(2022, 7, 12)); 
        String requestContent = objectMapper.writeValueAsString(petRequest);
        
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.NOT_FOUND,
                String.format(PET_NOT_FOUND, id));
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
    @WithUserDetails("user")
    void updatePet_Should_ReturnHttpStatusCode400_When_PetNameIsAlreadyTaken() throws Exception {
        long id = 101L;
        PetRequest petRequest = new PetRequest("Bobik", 101L, Gender.FEMAIL, LocalDate.of(2022, 7, 12)); 
        String requestContent = objectMapper.writeValueAsString(petRequest);
        
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.BAD_REQUEST,
                String.format(NAME_IS_ALREADY_TAKEN, petRequest.getName()));
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
    @WithUserDetails("user")
    void updatePet_Should_ReturnHttpStatusCode400_When_NewPetTypeNotFound() throws Exception {
        long typeId = 1000L;
        long id = 101L;
        
        PetRequest petRequest = new PetRequest("Fiska", typeId, Gender.FEMAIL, LocalDate.of(2022, 7, 12)); 
        String requestContent = objectMapper.writeValueAsString(petRequest);
        
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.BAD_REQUEST,
                String.format(TYPE_NOT_FOUND, typeId));
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
    @WithUserDetails("user2")
    void updatePet_Should_ReturnHttpStatusCode401_When_PetIsOwnedAnotherUser() throws Exception {
        
        long id = 101L;        
        PetRequest petRequest = new PetRequest("Fiska", 101L, Gender.FEMAIL, LocalDate.of(2022, 7, 12)); 
        String requestContent = objectMapper.writeValueAsString(petRequest);
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.UNAUTHORIZED,
                String.format(DONT_HAVE_PERMISSION));
        String expectedResponseContent = objectMapper.writeValueAsString(exceptionResponse);
 
        mockMvc.perform(put(REST_URL + "/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestContent)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResponseContent)); 

    }
    
    @Test
    @WithUserDetails("user")
    void updatePet_Should_ReturnFoundUpdatedPetEntry_When_PetEntryFound() throws Exception {
        
        long id = 101L;
        PetRequest petRequest = new PetRequest("Fiska", 101L, Gender.FEMAIL, LocalDate.of(2022, 7, 12)); 
        PetResponse petResponse = new PetResponse(id, "Fiska", "Cat", "FEMAIL", LocalDate.of(2022, 7, 12));
        String requestContent = objectMapper.writeValueAsString(petRequest);
        String expectedResponseContent = objectMapper.writeValueAsString(petResponse);
 
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
    @WithUserDetails("user")
    void deletePet_Should_ReturnHttpStatusCode404_When_PetEntryNotFound() throws Exception {
        
        long id = 1000L;        
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.NOT_FOUND,
                String.format(PET_NOT_FOUND, id));
        String expectedResponseContent = objectMapper.writeValueAsString(exceptionResponse);
 
        mockMvc.perform(delete(REST_URL + "/{id}", id))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResponseContent)); 

    }
    
    @Test
    @WithUserDetails("user2")
    void deletePet_Should_ReturnHttpStatusCode401_When_PetIsOwnedAnotherUser() throws Exception {
        
        long id = 101L;        
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.UNAUTHORIZED,
                String.format(DONT_HAVE_PERMISSION));
        String expectedResponseContent = objectMapper.writeValueAsString(exceptionResponse);
 
        mockMvc.perform(delete(REST_URL + "/{id}", id))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResponseContent)); 

    }
    
    @Test
    @WithUserDetails("user")
    void deletePet_Should_DeletedFoundPetEntry_When_PetEntryFound() throws Exception {
        
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
