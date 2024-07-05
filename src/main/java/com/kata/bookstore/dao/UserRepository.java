package com.kata.bookstore.dao;


import com.kata.bookstore.model.Order;
import com.kata.bookstore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
