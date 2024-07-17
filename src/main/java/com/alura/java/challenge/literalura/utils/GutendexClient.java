package com.alura.java.challenge.literalura.utils;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
public class GutendexClient {

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String BASE_URL = "https://gutendex.com/books/";

    public JsonNode getBookByTitle(String title) {
        ResponseEntity<String> response = restTemplate.getForEntity(BASE_URL + "?title=" + title, String.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                return new ObjectMapper().readTree(response.getBody());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

