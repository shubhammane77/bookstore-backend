package com.kata.bookstore.model.api;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AuthResponse {
    private int userId;
    private String userName;

    private boolean isSuccess;
    private String jwtToken;
    private String ErrorMessage;

}
