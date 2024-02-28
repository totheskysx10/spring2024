package com.spring.vsurin.bookexchange.domain;

/**
 * Сущность книги.
 * Каждая книга имеет id, название, автора, издательство,
 * год издания, ISBN, жанр, описание, доступность для обмена в данный момент и рейтинг.
 */
public class Book {
    /**
     * Уникальный идентификатор книги в базе.
     */
    private long id;

    /**
     * Название книги.
     */
    private String title;

    /**
     * Автор книги.
     */
    private String author;

    /**
     * Издательство.
     */
    private String publisher;

    /**
     * Год издания.
     */
    private int publicationYear;

    /**
     * Код ISBN.
     */
    private String isbn;

    /**
     * Жанр книги.
     */
    private String genre;

    /**
     * Описание книги.
     */
    private String description;

    /**
     * Доступность книги для обмена в данный момент.
     */
    private boolean availableForExchange;

    /**
     * Рейтинг книги.
     */
    private double rating;

}
