package com.kata.bookstore.service;

import com.kata.bookstore.dao.UserRepository;
import com.kata.bookstore.exception.InvalidInputException;
import com.kata.bookstore.model.User;
import com.kata.bookstore.model.api.AuthRequest;
import com.kata.bookstore.model.api.AuthResponse;
import com.kata.bookstore.security.jwt.JwtUtils;
import com.kata.bookstore.security.services.UserDetailsImpl;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

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


    /* Registers new User, returns error message if username/email already exists */
    public AuthResponse register(AuthRequest userRegistrationRequest) {
        AuthResponse authResponse = new AuthResponse();
        try {
            if (StringUtils.isEmpty(userRegistrationRequest.getUserName())
                    || StringUtils.isEmpty(userRegistrationRequest.getPassword())
                    || StringUtils.isEmpty(userRegistrationRequest.getEmailAddress())) {
                throw new InvalidInputException("Invalid inputs");
            }

            if(!validateEmailAddress(userRegistrationRequest.getEmailAddress())){
                throw new InvalidInputException("Invalid email address");
            }
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
                    userRegistrationRequest.getEmailAddress(),passwordEncoder.encode(userRegistrationRequest.getPassword()));
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

    /* Login existing user, generates JWT token, returns error message if user is not found or credentials are invalid */
    public AuthResponse login(AuthRequest authRequest) {
        AuthResponse authResponse = new AuthResponse();
        try {
            if (StringUtils.isEmpty(authRequest.getUserName())
                    || StringUtils.isEmpty(authRequest.getPassword())) {
                throw new InvalidInputException("Invalid inputs");
            }
            User existingUser = userRepository.findByUserName(authRequest.getUserName());
            if (existingUser == null) {
                throw new InvalidInputException("User name does not exist");
            }

            // Validate existing user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            String jwtToken = jwtUtils.generateJwtToken(authentication);

            authResponse.setJwtToken(jwtToken);
            authResponse.setUserId(existingUser.getId());
            authResponse.setUserName(existingUser.getUserName());
            return authResponse;
        } catch (InvalidInputException ex) {
            authResponse.setSuccess(false);
            authResponse.setErrorMessage(ex.getMessage());
            return authResponse;
        } catch (BadCredentialsException ex) {
            authResponse.setSuccess(false);
            authResponse.setErrorMessage("Invalid Credentials");
            return authResponse;
        } catch (Exception ex) {
            authResponse.setSuccess(false);
            throw ex;
        }
    }

    public boolean validateEmailAddress(String emailAddress) {
        String regexPattern =  "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return Pattern.matches(regexPattern, emailAddress);
    }

}
