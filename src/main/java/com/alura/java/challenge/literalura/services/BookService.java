package com.alura.java.challenge.literalura.services;

import com.alura.java.challenge.literalura.repositories.AuthorRepository;
import com.alura.java.challenge.literalura.repositories.BookRepository;
import com.alura.java.challenge.literalura.utils.GutendexClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.alura.java.challenge.literalura.models.Author;
import com.alura.java.challenge.literalura.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GutendexClient gutendexClient;

    public Book getBookByTitle(String title) {
        Book book = bookRepository.findByTitle(title);
        if (book == null) {
            try {
                JsonNode bookJson = gutendexClient.getBookByTitle(title);
                if (bookJson != null) {
                    book = new ObjectMapper().treeToValue(bookJson, Book.class);
                    Author author = new ObjectMapper().treeToValue(bookJson.path("authors").get(0), Author.class);
                    book.setAuthor(authorRepository.save(author));
                    book = bookRepository.save(book);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return book;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    public List<Book> getBooksByLanguage(String language) {
        return bookRepository.findByLanguages(language);
    }
    
    public long countBooksByLanguage(String language) {
        return bookRepository.findAll().stream()
                .filter(book -> book.getLanguages().equalsIgnoreCase(language))
                .count();
    }

    public Map<String, Long> countBooksByLanguages(List<String> languages) {
        List<Book> books = bookRepository.findAll();
        return languages.stream()
                .collect(Collectors.toMap(
                        language -> language,
                        language -> books.stream()
                                .filter(book -> book.getLanguages().equalsIgnoreCase(language))
                                .count()
                ));
    }
    
    public Book saveBookFromExternalAPI(Book book) {
        String apiUrl = "http://gutendex.com/books";

        // Construa a URL com os parâmetros necessários, como o título do livro
        String title = book.getTitle();
        String url = apiUrl + "?title=" + title;

        // Crie um cliente HTTP para fazer a requisição
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            // Faça a requisição para a API externa
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Verifique o código de status da resposta
            int statusCode = response.statusCode();
            if (statusCode == 200) { // Sucesso
                // Converta a resposta JSON em um objeto Book
                ObjectMapper mapper = new ObjectMapper();
                JsonNode responseBody = mapper.readTree(response.body());
                Book externalBook = mapper.treeToValue(responseBody, Book.class);

                // Salve o livro retornado no banco de dados local
                Book savedBook = bookRepository.save(externalBook);
                return savedBook;
            } else {
                // Caso contrário, lance uma exceção ou retorne null, dependendo da sua lógica
                throw new RuntimeException("Erro ao fazer a chamada à API externa. Código de status: " + statusCode);
            }
        } catch (IOException | InterruptedException e) {
            // Manipule qualquer exceção que possa ocorrer durante a requisição
            e.printStackTrace(); // Aqui, geralmente, você deve tratar a exceção adequadamente em um aplicativo real
            throw new RuntimeException("Erro ao fazer a chamada à API externa.", e);
        }
    }


}

