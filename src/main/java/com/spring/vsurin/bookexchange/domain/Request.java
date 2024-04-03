package com.spring.vsurin.bookexchange.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * Сущность, представляющая заявки на обмен.
 */
@Entity
@Table(name = "requests")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Request {
    /**
     * Уникальный идентификатор заявки.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    @Getter
    private long id;

    /**
     * Пользователь, отправивший заявку.
     */
    @ManyToOne
    @JoinColumn(name = "request_sender")
    @Getter
    @NonNull
    private User sender;

    /**
     * Пользователь, получивший заявку.
     */
    @ManyToOne
    @JoinColumn(name = "request_receiver")
    @Getter
    @NonNull
    private User receiver;

    /**
     * Книга, которую отправитель хочет получить.
     */
    @ManyToOne
    @JoinColumn(name = "request_book_sender_wants")
    @Getter
    @NonNull
    private Book bookSenderWants;

    /**
     * Книга, которую получатель хочет получить в обмен.
     */
    @ManyToOne
    @JoinColumn(name = "request_book_receiver_wants")
    @Getter
    @Setter
    private Book bookReceiverWants;

    /**
     * Статус заявки.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "request_status")
    @Getter
    @Setter
    private RequestStatus status;

    /**
     * Комментарий для получателя заявки.
     */
    @Column(name = "request_comment")
    @Getter
    @Setter
    private String commentForReceiver;
}
