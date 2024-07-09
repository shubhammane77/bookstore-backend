package com.kata.bookstore.service;

import com.kata.bookstore.dao.UserRepository;
import com.kata.bookstore.model.User;
import com.kata.bookstore.model.api.AuthRequest;
import com.kata.bookstore.security.jwt.JwtUtils;
import com.kata.bookstore.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.when;

public class UserServiceTests {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;
    @Mock
    JwtUtils jwtUtils;
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    Authentication authentication;
    @Mock
    PasswordEncoder passwordEncoder;
    private String existingUserName;
    private String existingEmailAddress;
    private String invalidEmail;

    private String newEmailAddress;
    private String newUserName;
    private String existingUserPassword;
    private String invalidPassword;
    private int validUserId;
    private String jwtToken;

    @BeforeEach
    public void setup() {


        MockitoAnnotations.initMocks(this);
        existingUserName = "Admin";
        existingEmailAddress = "test@test.com";
        existingUserPassword = "password";
        invalidPassword = "invalidPassword";
        newUserName="newUser";
        newEmailAddress = "newUser@test.com";
        invalidEmail = "tes-tgmail.com";
        validUserId = 1;
        jwtToken = "SECURE_TOKEN";
        User user = new User();
        user.setUserName(existingUserName);
        user.setEmailAddress(existingEmailAddress);
        user.setEncPassword(existingUserPassword);
        when(userRepository.findByUserName(existingUserName)).thenReturn(user);
        when(userRepository.findByEmailAddress(existingEmailAddress)).thenReturn(user);
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), invalidPassword))).thenThrow(BadCredentialsException.class);
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), existingUserPassword))).thenReturn(authentication);
        UserDetailsImpl userDetails = new UserDetailsImpl(validUserId, existingUserName, existingEmailAddress, existingUserPassword, null);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn(jwtToken);
        passwordEncoder = new BCryptPasswordEncoder();

    }

    @Test
    public void registerUser_shouldReturnErrorForEmptyData() {
        AuthRequest userRegistrationRequest = new AuthRequest();
        var result = userService.register(userRegistrationRequest);
        assert (result.getErrorMessage() != null && result.getErrorMessage().equals("Invalid inputs"));
    }

    @Test
    public void registerUser_shouldReturnErrorForInvalidEmail() {
        AuthRequest userRegistrationRequest = new AuthRequest();
        userRegistrationRequest.setUserName(existingUserName);
        userRegistrationRequest.setPassword(existingUserPassword);
        userRegistrationRequest.setEmailAddress(invalidEmail);
        var result = userService.register(userRegistrationRequest);
        assert (result.getErrorMessage() != null && result.getErrorMessage().equals("Invalid email address"));
    }
    @Test
    public void registerUser_shouldReturnErrorForExistingUserName() {
        AuthRequest userRegistrationRequest = new AuthRequest();
        userRegistrationRequest.setUserName(existingUserName);
        userRegistrationRequest.setPassword(existingUserPassword);
        userRegistrationRequest.setEmailAddress(existingEmailAddress);
        var result = userService.register(userRegistrationRequest);
        assert (result.getErrorMessage() != null && result.getErrorMessage().equals("User already exists with given username"));
    }

    @Test
    public void registerUser_shouldReturnErrorForExistingEmailAddress() {
        AuthRequest userRegistrationRequest = new AuthRequest();
        userRegistrationRequest.setEmailAddress(existingEmailAddress);
        userRegistrationRequest.setUserName(newUserName);
        userRegistrationRequest.setPassword(existingUserPassword);
        var result = userService.register(userRegistrationRequest);

        assert (result.getErrorMessage() != null && result.getErrorMessage().equals("User already exists with given email"));
    }

    @Test
    public void registerUser_shouldSuccess() {
        AuthRequest userRegistrationRequest = new AuthRequest();
        userRegistrationRequest.setEmailAddress(newEmailAddress);
        userRegistrationRequest.setUserName(newUserName);
        userRegistrationRequest.setPassword(existingUserPassword);
        var result = userService.register(userRegistrationRequest);
        assert (result.getErrorMessage() == null);
    }

    @Test
    public void loginUser_shouldReturnErrorForNonExistingUserName() {
        AuthRequest userLoginRequest = new AuthRequest();
        userLoginRequest.setUserName(newUserName);
        userLoginRequest.setPassword(existingUserPassword);
        var result = userService.login(userLoginRequest);
        assert (result.getErrorMessage() != null && result.getErrorMessage().equals("User name does not exist"));
    }

    @Test
    public void loginUser_shouldReturnErrorForInvalidPassword() {
        AuthRequest userLoginRequest = new AuthRequest();
        userLoginRequest.setUserName(existingUserName);
        userLoginRequest.setPassword(invalidPassword);
        var result = userService.login(userLoginRequest);
        assert (result.getErrorMessage() != null && result.getErrorMessage().equals("Invalid Credentials"));
    }

    @Test
    public void loginUser_shouldSuccess() {
        AuthRequest userRegistrationRequest = new AuthRequest();
        userRegistrationRequest.setEmailAddress(newEmailAddress);
        userRegistrationRequest.setUserName(newUserName);
        userRegistrationRequest.setPassword(existingUserPassword);
        var result = userService.register(userRegistrationRequest);
        assert (result.getErrorMessage() == null);
    }
}
