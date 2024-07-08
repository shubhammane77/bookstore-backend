package com.kata.bookstore.service;

import com.kata.bookstore.dao.UserRepository;
import com.kata.bookstore.model.User;
import com.kata.bookstore.model.api.AuthRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;

public class UserServiceTests {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createUser_shouldReturnErrorForExistingUserName() {
        AuthRequest userRegistrationRequest = new AuthRequest();
        String testUserName = "Admin";
        userRegistrationRequest.setUserName(testUserName);
        when(userRepository.findByUserName(testUserName)).thenReturn(new User());
        var result = userService.register(userRegistrationRequest);
        assert (result.getErrorMessage() != null && result.getErrorMessage().equals("User already exists with given username"));
    }

    @Test
    public void createUser_shouldReturnErrorForExistingEmailAddress() {
        AuthRequest userRegistrationRequest = new AuthRequest();
        String testEmailAddress = "test@test.com";
        String testUserName = "Admin";
        userRegistrationRequest.setEmailAddress(testEmailAddress);
        userRegistrationRequest.setUserName(testUserName);
        when(userRepository.findByEmailAddress(testEmailAddress)).thenReturn(new User());
        var result = userService.register(userRegistrationRequest);
        assert (result.getErrorMessage() != null && result.getErrorMessage().equals("User already exists with given email"));
    }
}
