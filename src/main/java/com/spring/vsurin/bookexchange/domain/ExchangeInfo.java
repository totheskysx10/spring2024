package com.spring.vsurin.bookexchange.domain;

public class ExchangeInfo {
    private User member; // участник обмена
    private Book exchangedBook; // книга, отправляемая данным участником
    private String address; // адрес проживания (доставки) данного участника
    private ExchangeStatus status; // статус обмена, установленный данным участником
    private String track; // трек-номер книги, отправленной данным участником
}
