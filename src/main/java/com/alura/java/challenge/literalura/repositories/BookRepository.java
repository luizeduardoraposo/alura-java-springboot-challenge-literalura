package com.alura.java.challenge.literalura.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.alura.java.challenge.literalura.models.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
    Book findByTitle(String title);
    List<Book> findByLanguages(String language);
}
