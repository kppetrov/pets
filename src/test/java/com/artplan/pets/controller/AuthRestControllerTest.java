package com.artplan.pets.controller;

import static com.artplan.pets.controller.AuthRestController.LOGIN_SUCCESSFULY;
import static com.artplan.pets.controller.AuthRestController.LOGOUT_SUCCESSFULY;
import static com.artplan.pets.controller.AuthRestController.REGISTER_SUCCESSFULY;
import static com.artplan.pets.security.LimitLoginAuthenticationProvider.LIMIT_IS_OWER_MSG;
import static com.artplan.pets.service.impl.UserServiceImpl.USERNAME_IS_TAKEN_MSG;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
import com.artplan.pets.dto.LoginRequest;
import com.artplan.pets.utils.AppConstants;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = { "/insert-user-test-data.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class AuthRestControllerTest {
    
    final static String REST_URL = "/api/auth";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void login_Should_Authenticated_When_ValidUser() throws Exception {
        
        LoginRequest loginRequest = new LoginRequest("user", "user");
        String requestContent = objectMapper.writeValueAsString(loginRequest);

        ApiResponse apiResponse = new ApiResponse(true, LOGIN_SUCCESSFULY);
        String expectedResponseContent = objectMapper.writeValueAsString(apiResponse);

        mockMvc.perform(post(REST_URL + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestContent)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResponseContent))
                .andExpect(authenticated().withUsername("user"));
        
    }
    
    @Test
    void login_Should_Unauthenticated_When_NotValidUser() throws Exception {
        
        LoginRequest loginRequest = new LoginRequest("user", "user1");
        String requestContent = objectMapper.writeValueAsString(loginRequest);

        mockMvc.perform(post(REST_URL + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestContent)
                )
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(unauthenticated());        
    }
    
    @Test
    void register_Should_ReturnHttpStatusCode400AndUnauthenticated_When_NewUsernamiIsTaken() throws Exception {
        
        LoginRequest loginRequest = new LoginRequest("user", "user");
        String requestContent = objectMapper.writeValueAsString(loginRequest);

        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.BAD_REQUEST,
                String.format(USERNAME_IS_TAKEN_MSG, loginRequest.getUsername()));
        String expectedResponseContent = objectMapper.writeValueAsString(exceptionResponse);

        mockMvc.perform(post(REST_URL + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestContent)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResponseContent))
                .andExpect(unauthenticated());
        
    }
    
    @Test
    void register_Should_AddNewUserAndLogin_When_ValidUser() throws Exception {
        
        LoginRequest loginRequest = new LoginRequest("newuser", "newuser");
        String requestContent = objectMapper.writeValueAsString(loginRequest);

        ApiResponse apiResponse = new ApiResponse(true, REGISTER_SUCCESSFULY);
        String expectedResponseContent = objectMapper.writeValueAsString(apiResponse);

        mockMvc.perform(post(REST_URL + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestContent)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResponseContent))
                .andExpect(authenticated().withUsername("newuser"));
        
    }
    
    @Test
    @WithUserDetails("admin")
    void logout_Should_Logout() throws Exception {

        ApiResponse apiResponse = new ApiResponse(true, LOGOUT_SUCCESSFULY);
        String expectedResponseContent = objectMapper.writeValueAsString(apiResponse);

        mockMvc.perform(post(REST_URL + "/logout"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResponseContent))
                .andExpect(unauthenticated());
        
    }
    
    @Test
    void login_Should_ReturnHttpStatusCode401AndUnauthenticated_When_ManyFailAttempt() throws Exception {
        
        LoginRequest loginRequest = new LoginRequest("user", "user1");
        String requestContent = objectMapper.writeValueAsString(loginRequest);
        
        for (int i = 0; i <= AppConstants.FAIL_ATTEMPT_LIMIT; i++) {
            mockMvc.perform(post(REST_URL + "/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestContent)
                    );
        }        

        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.UNAUTHORIZED,
                String.format(LIMIT_IS_OWER_MSG, loginRequest.getUsername()));
        String expectedResponseContent = objectMapper.writeValueAsString(exceptionResponse);

        mockMvc.perform(post(REST_URL + "/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestContent)
                )
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedResponseContent))
                .andExpect(unauthenticated());
        
    }

}
