package com.alura.java.challenge.literalura.controllers;

import com.alura.java.challenge.literalura.services.BookService;
import com.alura.java.challenge.literalura.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.getAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        if (book != null) {
            return new ResponseEntity<>(book, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    
    @PostMapping("/external")
    public ResponseEntity<Book> saveBookFromExternalAPI(@RequestBody Book book) {
        Book savedBook = bookService.saveBookFromExternalAPI(book);
        return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
    }


    @GetMapping("/title/{title}")
    public ResponseEntity<Book> getBookByTitle(@PathVariable String title) {
        Book book = bookService.getBookByTitle(title);
        if (book != null) {
            return new ResponseEntity<>(book, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/language/{language}")
    public ResponseEntity<List<Book>> getBooksByLanguage(@PathVariable String language) {
        List<Book> books = bookService.getBooksByLanguage(language);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }
    
    @GetMapping("/count/{language}")
    public ResponseEntity<Long> countBooksByLanguage(@PathVariable String language) {
        long count = bookService.countBooksByLanguage(language);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @GetMapping("/count-languages")
    public ResponseEntity<Map<String, Long>> countBooksByLanguages(@RequestParam List<String> languages) {
        Map<String, Long> counts = bookService.countBooksByLanguages(languages);
        return new ResponseEntity<>(counts, HttpStatus.OK);
    }
}