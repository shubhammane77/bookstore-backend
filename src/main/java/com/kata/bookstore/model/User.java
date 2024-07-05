package com.kata.bookstore.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class User {
    private int id;
    private String userName;
    private String firstName;
    private String lastName;
    private String emailAddress;

    public User(String userName, String firstName, String lastName, String emailAddress) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
    }
}
