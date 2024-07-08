package com.kata.bookstore.model.api;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AuthRequest {
    private String userName;
    private String emailAddress;
    private String password;

}
