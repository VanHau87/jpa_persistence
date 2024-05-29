package com.hnguyen387.jpa_persistence.ch05.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hnguyen387.jpa_persistence.ch05.entities.Book;

public interface BookRepository extends JpaRepository<Book, Long>{

}
