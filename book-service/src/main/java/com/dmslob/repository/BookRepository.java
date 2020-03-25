package com.dmslob.repository;

import com.dmslob.domain.Book;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public class BookRepository {
    private List<Book> books = Arrays.asList(
            new Book("1", "Lord of the Flies"),
            new Book("2", "Being and Nothingness"),
            new Book("3", "At Sea and Lost"),
            new Book("4", "The Bad Seed"),
            new Book("5", "How to Draw 101 Animals")
    );

    public List<Book> findAll() {
        return books;
    }
}
