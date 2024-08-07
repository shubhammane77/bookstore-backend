package com.kata.bookstore.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String userName;
    @Column
    private String emailAddress;
    @Column
    private String encPassword;

    public User(String userName, String emailAddress, String encPassword) {
        this.userName = userName;
        this.emailAddress = emailAddress;
        this.encPassword = encPassword;
    }
}
