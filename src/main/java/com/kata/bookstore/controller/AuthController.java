package com.kata.bookstore.controller;

// AuthController.java

import com.kata.bookstore.model.api.AuthRequest;
import com.kata.bookstore.model.api.AuthResponse;
import com.kata.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/v1/auth")

public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest user) {
        var result = userService.register(user);
        if (result.getErrorMessage() != null) {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
        return new ResponseEntity(result, CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest user) {
        var result = userService.login(user);
        if(result == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        return ResponseEntity.ok(result);
    }
}