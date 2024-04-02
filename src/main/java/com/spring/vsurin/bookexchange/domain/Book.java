package com.spring.vsurin.bookexchange.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Year;
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
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class Book {
    /**
     * Уникальный идентификатор книги в базе.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    @Getter
    @EqualsAndHashCode.Include
    private long id;

    /**
     * Пользователи, у которых такая книга сейчас в библиотеке.
     */
    @ManyToMany(mappedBy = "library", fetch = FetchType.EAGER)
    @Getter
    private List<User> owners;

    /**
     * Название книги.
     */
    @Column(name = "book_title")
    @Getter
    @EqualsAndHashCode.Include
    private String title;

    /**
     * Автор книги.
     */
    @Column(name = "book_author")
    @Getter
    @EqualsAndHashCode.Include
    private String author;

    /**
     * Год издания.
     */
    @Column(name = "book_year")
    @Getter
    @EqualsAndHashCode.Include
    private Year publicationYear;

    /**
     * Код ISBN.
     */
    @Column(name = "book_isbn")
    @Getter
    @EqualsAndHashCode.Include
    private String isbn;

    /**
     * Жанр книги.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "book_genre")
    @Getter
    @EqualsAndHashCode.Include
    private BookGenre genre;

    /**
     * Описание книги.
     */
    @Column(name = "book_description")
    @Getter
    @Setter
    @EqualsAndHashCode.Include
    private String description;

    /**
     * Пользователи, предлагающие книгу для обмена в данный момент.
     */
    @ManyToMany(mappedBy = "offeredBooks", fetch = FetchType.EAGER)
    @Getter
    private List<User> usersOfferingForExchange;

    /**
     * Рейтинг книги.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "book_marks", joinColumns = @JoinColumn(name = "book_id"))
    private List<Integer> marks;

    /**
     * Изображение обложки книги в виде массива байтов.
     */
    @Lob
    @Column(name = "book_cover")
    @Getter
    @Setter
    private byte[] coverImage;

    /**
     * Возвращает итератор для оценок книги.
     *
     * @return итератор для оценок книги
     */
    public Iterable<Integer> getMarks() {
        return marks;
    }

    /**
     * Вычисляет рейтинг книги.
     *
     * @return рейтинг книги или 0, если книга не найдена или у неё нет оценок
     */
    public double calculateBookRating() {
        double result = 0;
            if (marks.isEmpty()) {
                return 0;
            }
            double sum = 0;
            for (int mark : marks) {
                sum += mark;
            }
            result = sum / marks.size();
        return result;
    }

    /**
     * Добавляет оценку книге.
     *
     * @param mark   оценка, которую нужно добавить
     */
    public void addMarkToBook(int mark) {
        if (mark >= 1 && mark <= 10) {
            marks.add(mark);
        }
    }
}