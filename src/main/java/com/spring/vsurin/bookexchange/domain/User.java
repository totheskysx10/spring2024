package com.spring.vsurin.bookexchange.domain;

import java.util.List;

public class User {
    private long id; // уникальный идентификатор пользователя в базе
    private String username; // имя пользователя
    private String email; // адрес эл. почты пользователя
    private List<Book> userLibrary; // библиотека пользователя (книги, которые у него есть)
    private List<String> addressList; // список адресов проживания пользователя, куда можно доставлять книги при обмене
    private int phoneNumber; // номер телефона
}
