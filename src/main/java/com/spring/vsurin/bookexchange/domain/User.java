package com.spring.vsurin.bookexchange.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Сущность пользователя.
 * Каждый пользователь имеет id, имя, email,
 * библиотеку, список книг для обмена в данный момент, список адресов и номер телефона.
 */
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    /**
     * Уникальный идентификатор пользователя в базе.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    @Getter
    private long id;

    /**
     * Имя пользователя.
     */
    @Column(name = "user_name")
    @Getter
    @Setter
    private String username;

    /**
     * Адрес электронной почты пользователя.
     */
    @Column(name = "user_email")
    @Getter
    @Setter
    private String email;

    /**
     * Библиотека пользователя (книги, которые у него есть).
     */
    @ManyToMany
    @JoinTable(
            name = "user_books",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    @Getter
    private List<Book> library;

    /**
     * Книги, которые пользователь готов обменять.
     */
    @ManyToMany
    @JoinTable(
            name = "user_offered_books",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    @Getter
    private List<Book> offeredBooks;

    /**
     * Список адресов проживания пользователя, куда можно доставлять книги при обмене.
     */
    @ElementCollection
    @CollectionTable(name = "user_addresses", joinColumns = @JoinColumn(name = "user_id"))
    @Getter
    @Setter
    private List<String> addressList;

    /**
     * Номер телефона пользователя.
     */
    @Column(name = "user_phone")
    @Getter
    @Setter
    private String phoneNumber;
}