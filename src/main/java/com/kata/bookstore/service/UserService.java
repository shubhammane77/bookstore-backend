package com.kata.bookstore.service;

import com.kata.bookstore.dao.UserRepository;
import com.kata.bookstore.exception.InvalidInputException;
import com.kata.bookstore.model.User;
import com.kata.bookstore.model.api.AuthRequest;
import com.kata.bookstore.model.api.AuthResponse;
import com.kata.bookstore.security.jwt.JwtUtils;
import com.kata.bookstore.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    JwtUtils jwtUtils;


    public AuthResponse register(AuthRequest userRegistrationRequest) {
        AuthResponse authResponse = new AuthResponse();
        try {
            User existingUser = userRepository.findByUserName(userRegistrationRequest.getUserName());
            if (existingUser != null) {
                throw new InvalidInputException("User already exists with given username");
            }

            existingUser = userRepository.findByEmailAddress(userRegistrationRequest.getEmailAddress());
            if (existingUser != null) {
                throw new InvalidInputException("User already exists with given email");
            }

            // Create new user's account
            User user = new User(userRegistrationRequest.getUserName(),
                    userRegistrationRequest.getEmailAddress());
            user.setEncPassword(passwordEncoder.encode(userRegistrationRequest.getPassword()));
            userRepository.save(user);
            authResponse.setSuccess(true);
            return authResponse;
        } catch (InvalidInputException ex) {
            authResponse.setSuccess(false);
            authResponse.setErrorMessage(ex.getMessage());
            return authResponse;
        } catch (Exception ex) {
            authResponse.setSuccess(false);
            authResponse.setErrorMessage("Error while creating user");
            return authResponse;
        }
    }

    public AuthResponse login(AuthRequest authRequest) {
        AuthResponse authResponse = new AuthResponse();
        try {
            User existingUser = userRepository.findByUserName(authRequest.getUserName());
            if (existingUser == null) {
                throw new InvalidInputException("User name does not exist");
            }

            // Validate existing user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            authResponse.setUserId(existingUser.getId());
            authResponse.setUserName(existingUser.getUserName());
            authResponse.setJwtToken(jwt);
            return authResponse;
        } catch (InvalidInputException ex) {
            authResponse.setSuccess(false);
            authResponse.setErrorMessage(ex.getMessage());
            return authResponse;
        } catch (Exception ex) {
            authResponse.setSuccess(false);
            authResponse.setErrorMessage("Invalid Credentials");
            return authResponse;
        }
    }
}
