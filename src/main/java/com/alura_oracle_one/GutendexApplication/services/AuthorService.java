package com.alura_oracle_one.GutendexApplication.services;

import com.alura_oracle_one.GutendexApplication.models.Author;
import com.alura_oracle_one.GutendexApplication.repositories.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public Author getAuthorById(Long id) {
        return authorRepository.findById(id).orElse(null);
    }

    public Author saveAuthor(Author author) {
        return authorRepository.save(author);
    }

    public List<Author> getAuthorsByCountry(String country) {
        return authorRepository.findByCountry(country);
    }

    // Nova funcionalidade para encontrar autores vivos em um determinado ano
    public List<Author> getAuthorsAliveInYear(int year) {
        return authorRepository.findByBirthYearLessThanEqualAndDeathYearGreaterThanEqualOrDeathYearIsNull(year, year);
    }

    // Exemplo de uso:
    @GetMapping("/alive-in-year/{year}")
    public ResponseEntity<List<Author>> getAuthorsAliveInYearEndpoint(@PathVariable int year) {
        List<Author> authors = getAuthorsAliveInYear(year);
        return new ResponseEntity<>(authors, HttpStatus.OK);
    }
}
