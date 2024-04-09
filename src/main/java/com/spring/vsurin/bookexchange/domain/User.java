package com.spring.vsurin.bookexchange.domain;

import jakarta.persistence.*;
import lombok.*;

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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class User {
    /**
     * Уникальный идентификатор пользователя в базе.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    @Getter
    @EqualsAndHashCode.Include
    private long id;

    /**
     * Имя пользователя.
     */
    @Column(name = "user_name")
    @Getter
    private String username;

    /**
     * Пол пользователя.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "user_gender")
    @Getter
    @NonNull
    private UserGender gender;

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
    @ManyToMany(fetch = FetchType.EAGER)
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
    @ManyToMany(fetch = FetchType.EAGER)
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
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_addresses", joinColumns = @JoinColumn(name = "user_id"))
    @Getter
    private List<String> addressList;

    /**
     * Номер телефона пользователя.
     */
    @Column(name = "user_phone")
    @Getter
    @Setter
    private String phoneNumber;

    /**
     * Список обменов пользователя, как member 1.
     */
    @OneToMany(mappedBy = "member1", fetch = FetchType.EAGER)
    @Getter
    private List<Exchange> exchangesAsMember1;

    /**
     * Список обменов пользователя, как member 2.
     */
    @OneToMany(mappedBy = "member2", fetch = FetchType.EAGER)
    @Getter
    private List<Exchange> exchangesAsMember2;

    /**
     * Основной адрес доставки пользователя, используемый в данный момент.
     */
    @Column(name = "main_address")
    @Getter
    @Setter
    @NonNull
    private String mainAddress;
}