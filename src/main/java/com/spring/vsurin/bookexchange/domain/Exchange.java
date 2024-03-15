package com.spring.vsurin.bookexchange.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * Класс, представляющий обменную операцию.
 * Каждый обмен имеет id, информацию о первом и втором его участниках,
 * статус обмена и дату создания заявки.
 */
@Entity
@Table(name = "exchanges")
@AllArgsConstructor
@NoArgsConstructor
public class Exchange {
    /**
     * Уникальный идентификатор обмена.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exchange_id")
    @Getter
    private long id;

    /**
     * Участник обмена 1.
     */
    @ManyToOne
    @JoinColumn(name = "member1")
    @Getter
    @Setter
    private User member1;

    /**
     * Участник обмена 2.
     */
    @ManyToOne
    @JoinColumn(name = "member2")
    @Getter
    @Setter
    private User member2;

    /**
     * Книга, отправляемая участником 1.
     */
    @ManyToOne
    @JoinColumn(name = "book1")
    @Getter
    @Setter
    private Book exchangedBook1;

    /**
     * Книга, отправляемая участником 2.
     */
    @ManyToOne
    @JoinColumn(name = "book2")
    @Getter
    @Setter
    private Book exchangedBook2;

    /**
     * Адрес проживания (доставки) участника 1.
     */
    @Column(name = "address1")
    @Getter
    @Setter
    private String address1;

    /**
     * Адрес проживания (доставки) участника 2.
     */
    @Column(name = "address2")
    @Getter
    @Setter
    private String address2;

    /**
     * Трек-номер книги, отправленной участником 1.
     */
    @Column(name = "track1")
    @Getter
    @Setter
    private String track1;

    /**
     * Трек-номер книги, отправленной участником 2.
     */
    @Column(name = "track2")
    @Getter
    @Setter
    private String track2;

    /**
     * Подтверждение получения книги участником 1.
     */
    @Column(name = "received1")
    @Getter
    @Setter
    private boolean received1;

    /**
     * Подтверждение получения книги участником 2.
     */
    @Column(name = "received2")
    @Getter
    @Setter
    private boolean received2;

    /**
     * Статус обмена.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @Getter
    @Setter
    private ExchangeStatus status;

    /**
     * Дата создания заявки.
     */
    @Column(name = "date")
    @Getter
    @Setter
    private Date date;
}