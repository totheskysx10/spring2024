package com.spring.vsurin.bookexchange.domain;

import java.util.List;

/**
 * Сущность пользователя.
 * Каждый пользователь имеет id, имя, email,
 * библиотеку, список адресов и номер телефона.
 */
public class User {
    /**
     * Уникальный идентификатор пользователя в базе.
     */
    private long id;

    /**
     * Имя пользователя.
     */
    private String username;

    /**
     * Адрес электронной почты пользователя.
     */
    private String email;

    /**
     * Библиотека пользователя (книги, которые у него есть).
     */
    private List<Book> userLibrary;

    /**
     * Список адресов проживания пользователя, куда можно доставлять книги при обмене.
     */
    private List<String> addressList;

    /**
     * Номер телефона пользователя.
     */
    private int phoneNumber;

}
