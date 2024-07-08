package com.kata.bookstore.dao;


import com.kata.bookstore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    public User findByUserName(String userName);
    public User findByEmailAddress(String emailAddress);

}
