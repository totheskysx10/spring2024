package com.spring.vsurin.bookexchange.domain;

public class Book {
    private long id; // уникальный идентификатор книги в базе
    private String title; // название книги
    private String author; // автор книги
    private String publisher; // издательство
    private int publicationYear; // год издания
    private String isbn; // код ISBN
    private String genre; // жанр
    private String description; // описание
    private boolean availableForExchange; // доступность книги для обмена в данный момент
    private double rating; // рейтинг книги
}
