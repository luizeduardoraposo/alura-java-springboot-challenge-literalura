package com.alura.java.challenge.literalura.repositories;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.alura.java.challenge.literalura.models.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    Author findByName(String name);
    List<Author> findByCountry(String country);
    
    List<Author> findByBirthYearLessThanEqualAndDeathYearGreaterThanEqualOrDeathYearIsNull(int year, int year2);
}
