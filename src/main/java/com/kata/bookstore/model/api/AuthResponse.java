package com.kata.bookstore.model.api;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class AuthResponse implements Serializable {
    private int userId;
    private String userName;
    private boolean isSuccess;
    private String jwtToken;
    private String ErrorMessage;

}
