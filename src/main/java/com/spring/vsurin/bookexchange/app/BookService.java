package com.spring.vsurin.bookexchange.app;

import com.spring.vsurin.bookexchange.domain.Book;
import com.spring.vsurin.bookexchange.domain.BookGenre;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Сервисный класс для работы с книгами.
 */
@Slf4j
@Component
public class BookService {

    private final BookRepository bookRepository;


    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Создает новую книгу и, если не null, сохраняет её в базе данных, проверяя, нет ли ещё такой книги.
     * @param book объект книги для создания
     * @return сохраненная книга
     * @throws IllegalStateException если книга равна null
     */
    public Book createBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Книга не может быть null");
        }

        try {
            List<Book> existingBooks = bookRepository.findByTitleAndAuthorIgnoreCase(book.getTitle(), book.getAuthor());
            if (!existingBooks.isEmpty()) {
                log.error("Книга с названием {} и автором {} уже есть в базе, добавление не выполнено", book.getTitle(), book.getAuthor());
                return book;
            }
            bookRepository.save(book);
            log.info("Создана книга с id {}", book.getId());
            return book;
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при создании книги", e);
        }
    }

    /**
     * Получает книгу по её идентификатору.
     * @param bookId идентификатор книги
     * @return найденная книга
     */
    public Book getBookById(long bookId) {
        Book foundBook = bookRepository.findById(bookId);
        if (foundBook == null) {
            throw new IllegalArgumentException("Книга с id " + bookId + " не найдена");
        }
        else {
           log.info("Найдена книга с id {}", bookId);
            return foundBook;
        }
    }

    /**
     * Возвращает все книги, которые есть в базе, с пагинацией.
     * @return все книги, по страницам
     */
    public Page<Book> getAllBooksInBase(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    /**
     * Ищет книги по указанному жанру, с пагинацией.
     *
     * @param genre жанр книги, по которому нужно выполнить поиск
     * @return список книг, соответствующих указанному жанру, по страницам
     */
    public Page<Book> searchByGenre(BookGenre genre, Pageable pageable) {
        return bookRepository.findByGenre(genre, pageable);
    }

    /**
     * Ищет книги по заданной строке, которая может быть как названием книги, так и именем автора.
     *
     * @param searchTerm строка, по которой будет выполнен поиск; может быть как названием книги, так и именем автора
     * @return список книг, у которых либо название книги, либо имя автора содержит указанную строку
     */
    public Page<Book> searchByTitleOrAuthor(String searchTerm, Pageable pageable) {
        return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(searchTerm, searchTerm, pageable);
    }

    /**
     * Получает список книг доступных для обмена.
     * Запрашивает базу данных на предмет книг, у которых есть хотя бы один пользователь, предлагающий их для обмена.
     *
     * @return Список книг, доступных для обмена.
     */
    public Page<Book> getAvailableForExchangeBooks(Pageable pageable) {
        return bookRepository.findBooksWithUsersOfferingForExchange(pageable);
    }

    /**
     * Добавляет оценку книге с указанным идентификатором, проверяя, подходит ли оценка под условия.
     *
     * @param bookId идентификатор книги
     * @param mark   оценка, которую нужно добавить
     */
    public void addMarkToBook(long bookId, int mark) {
        Book book = getBookById(bookId);
        if (book != null) {
            if (mark >= 1 && mark <= 10) {
                book.addMarkToBook(mark);
                bookRepository.save(book);
                log.info("Оценка {} добавлена в список оценок книги с id {}", mark, bookId);
            } else
                log.error("Оценка {} не добавлена в список оценок книги с id {} - она должна быть от 1 до 10", mark, bookId);
        } else
            log.error("Оценка {} не добавлена в список оценок книги с id {} - книга не должна быть null", mark, bookId);
    }

    /**
     * Добавляет описание книге с указанным идентификатором.
     *
     * @param bookId идентификатор книги
     * @param desc   описание, которое нужно добавить
     */
    public void updateDescriptionToBook(long bookId, String desc) {
        Book book = getBookById(bookId);
        if (book != null) {
            book.setDescription(desc);
            bookRepository.save(book);
            log.info("Описание добавлено для книги с id {}", bookId);
        }
    }

    /**
     * Получает изображение обложки книги по её идентификатору.
     *
     * @param bookId идентификатор книги
     * @return изображение обложки книги в виде массива байтов
     */
    public byte[] getBookCover(long bookId) {
        Book book = getBookById(bookId);
        return book.getCoverImage();
    }

    /**
     * Обновляет изображение обложки книги по её идентификатору.
     *
     * @param bookId идентификатор книги
     * @param image новое изображение обложки книги в виде массива байтов
     */
    public void updateBookCover(long bookId, byte[] image) {
        Book book = getBookById(bookId);
        if (book != null) {
            book.setCoverImage(image);
            bookRepository.save(book);
            log.info("Обложка добавлена/обновлена для книги с id {}", bookId);
        }
    }
}