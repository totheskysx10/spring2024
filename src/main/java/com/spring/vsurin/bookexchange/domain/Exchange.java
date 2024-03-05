package com.spring.vsurin.bookexchange.domain;

import java.util.Date;

public class Exchange {
    private long id; // уникальный идентификатор обмена
    private ExchangeInfo exchangeInfoUser1; // информация о первом участнике обмена, его книга и инфо о доставке
    private ExchangeInfo exchangeInfoUser2; // информация о втором участнике обмена, его книга и инфо о доставке
    private Date date; // дата создания заявки
}


