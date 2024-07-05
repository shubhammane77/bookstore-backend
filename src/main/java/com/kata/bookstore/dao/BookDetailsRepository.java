package com.kata.bookstore.dao;

import com.kata.bookstore.model.BookDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookDetailsRepository extends JpaRepository<BookDetails, Integer> {
}
