package com.kata.bookstore.dao;


import com.kata.bookstore.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookPagingRepository extends PagingAndSortingRepository<Book, Integer> {
    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
