package com.spring.vsurin.bookexchange.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.util.List;

/**
 * Сущность книги.
 * Каждая книга имеет id, название, автора, издательство,
 * год издания, ISBN, жанр, описание, список пользователей, готовых её обменять, и список оценок (для формирования рейтинга).
 */
@Entity
@Table(name = "books")
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    /**
     * Уникальный идентификатор книги в базе.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    @Getter
    private long id;

    /**
     * Пользователи, у которых такая книга сейчас в библиотеке.
     */
    @ManyToMany(mappedBy = "ownedBooks")
    @Getter
    private List<User> owners;

    /**
     * Название книги.
     */
    @Column(name = "book_title")
    @Getter
    @Setter
    private String title;

    /**
     * Автор книги.
     */
    @Column(name = "book_author")
    @Getter
    @Setter
    private String author;

    /**
     * Год издания.
     */
    @Column(name = "book_publicationYear")
    @Getter
    @Setter
    private int publicationYear;

    /**
     * Код ISBN.
     */
    @Column(name = "book_isbn")
    @Getter
    @Setter
    private String isbn;

    /**
     * Жанр книги.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "book_genre")
    @Getter
    @Setter
    private BookGenre genre;

    /**
     * Описание книги.
     */
    @Column(name = "book_description")
    @Getter
    @Setter
    private String description;

    /**
     * Пользователи, предлагающие книгу для обмена в данный момент.
     */
    @ManyToMany(mappedBy = "offeredBooks")
    @Getter
    private List<User> usersOfferingForExchange;

    /**
     * Рейтинг книги.
     */
    @ElementCollection
    @CollectionTable(name = "book_marks", joinColumns = @JoinColumn(name = "book_id"))
    @Getter
    @Setter
    @Range(min = 1, max = 10)
    private List<Integer> marks;
}