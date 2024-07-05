package com.kata.bookstore.dao;


import com.kata.bookstore.model.Author;
import com.kata.bookstore.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
}
