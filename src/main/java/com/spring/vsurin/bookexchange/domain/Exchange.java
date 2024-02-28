package com.spring.vsurin.bookexchange.domain;

import java.util.Date;

/**
 * Класс, представляющий обменную операцию.
 * Каждый обмен имеет id, информацию о первом и втором его участниках,
 * статус обмена и дату создания заявки.
 */
public class Exchange {
    /**
     * Уникальный идентификатор обмена.
     */
    private long id;

    /**
     * Информация о первом участнике обмена, его книга и информация о доставке.
     */
    private ExchangeInfo exchangeInfoUser1;

    /**
     * Информация о втором участнике обмена, его книга и информация о доставке.
     */
    private ExchangeInfo exchangeInfoUser2;

    /**
     * Статус обмена.
     */
    private ExchangeStatus status;

    /**
     * Дата создания заявки.
     */
    private Date date;

}


