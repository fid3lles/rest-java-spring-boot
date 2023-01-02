package br.com.fidelles.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fidelles.book.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> { }
