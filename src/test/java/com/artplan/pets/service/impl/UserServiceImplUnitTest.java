package com.artplan.pets.service.impl;

import static com.artplan.pets.service.impl.UserServiceImpl.USERNAME_IS_TAKEN_MSG;
import static com.artplan.pets.utils.AppConstants.DEFOULT_ROLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;

import com.artplan.pets.dto.UserIdentityAvailability;
import com.artplan.pets.entity.User;
import com.artplan.pets.exception.BadRequestException;
import com.artplan.pets.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@TestPropertySource("/application-test.properties")
class UserServiceImplUnitTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    UserServiceImpl service;


    @Test
    void addUserWithDefaultRole_Should_ThrowException_When_UsernameIsAlreadyTaken() {
        
        User user = new User("user", "user");
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(true);
        String expectedMsg = String.format(USERNAME_IS_TAKEN_MSG, user.getUsername());
        BadRequestException exception = assertThrows(BadRequestException.class, () -> service.addUserWithDefaultRole(user));
        assertEquals(expectedMsg, exception.getMessage()); 
        
    }

    @Test
    void addUserWithDefaultRole_Should_SaveUserWithDefaultRole_When_UsernameIsAvailable() {
        
        User user = new User("user", "user");
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);
        service.addUserWithDefaultRole(user);
        ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(argument.capture());
        assertEquals(DEFOULT_ROLE, argument.getValue().getRole());
        
    }
    
    @Test
    void checkUsernameAvailability_Should_ReturnUsernameAvailability() {
        
        String ExistingUsername = "ExistingUsername";
        String NotExistingUsername = "NotExistingUsername";        
        UserIdentityAvailability expectedIsAvailable = new UserIdentityAvailability(true);
        UserIdentityAvailability expectedIsNotAvailable = new UserIdentityAvailability(false);
        
        when(userRepository.existsByUsername(ExistingUsername)).thenReturn(true);
        when(userRepository.existsByUsername(NotExistingUsername)).thenReturn(false);        
        
        assertEquals(service.checkUsernameAvailability(NotExistingUsername), expectedIsAvailable);
        assertEquals(service.checkUsernameAvailability(ExistingUsername), expectedIsNotAvailable);
        
    }
    
}
