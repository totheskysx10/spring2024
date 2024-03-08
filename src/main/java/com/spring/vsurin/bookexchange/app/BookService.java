package com.spring.vsurin.bookexchange.app;

import com.spring.vsurin.bookexchange.domain.Book;
import com.spring.vsurin.bookexchange.domain.BookGenre;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Сервисный класс для работы с книгами.
 */
@Slf4j
@Component
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Создает новую книгу и сохраняет его в базе данных, проверяя, нет ли ещё такой книги.
     * @param book объект книги для создания
     * @return сохраненная книга
     */
    public Book createBook(Book book) {
        List<Book> existingBooks = bookRepository.findByTitleAndAuthorIgnoreCase(book.getTitle(), book.getAuthor());
        if (!existingBooks.isEmpty()) {
            log.warn("Книга с названием {} и автором {} уже есть в базе, добавление не выполнено", book.getTitle(), book.getAuthor());
            return null;
        }
        bookRepository.save(book);
        log.info("Создана книга с id {}", book.getId());
        return book;
    }

    /**
     * Получает книгу по её идентификатору.
     * @param bookId идентификатор книги
     * @return найденная книга
     */
    public Book getBookById(long bookId) {
        Book foundBook = bookRepository.findById(bookId);
        if (foundBook == null)
            log.error("Не найдена книга с id {}", bookId);
        else {
            log.info("Найдена книга с id {}", bookId);
            return foundBook;
        }
        return null;
    }

    /**
     * Возвращает все книги, которые есть в базе.
     * @return все книги
     */
    public List<Book> getAllBooksInBase() {
        List<Book> AllBooks = bookRepository.findAll();
        return AllBooks;
    }

    /**
     * Ищет книги по указанному жанру.
     *
     * @param genre жанр книги, по которому нужно выполнить поиск
     * @return список книг, соответствующих указанному жанру
     */
    public List<Book> searchByGenre(BookGenre genre) {
        return bookRepository.findByGenreIgnoreCase(genre);
    }

    /**
     * Ищет книги по заданной строке, которая может быть как названием книги, так и именем автора.
     *
     * @param searchTerm строка, по которой будет выполнен поиск; может быть как названием книги, так и именем автора
     * @return список книг, у которых либо название книги, либо имя автора содержит указанную строку
     */
    public List<Book> searchByTitleOrAuthor(String searchTerm) {
        return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(searchTerm, searchTerm);
    }

    /**
     * Получает список книг доступных для обмена.
     * Запрашивает базу данных на предмет книг, у которых есть хотя бы один пользователь, предлагающий их для обмена.
     *
     * @return Список книг, доступных для обмена.
     */
    public List<Book> getAvailableForExchanheBooks() {
        List<Book> availableForExchangeBooks = bookRepository.findBooksWithUsersOfferingForExchange();
        return availableForExchangeBooks;
    }

    /**
     * Добавляет оценку книге с указанным идентификатором.
     *
     * @param bookId идентификатор книги
     * @param mark   оценка, которую нужно добавить
     */
    public void addMarkToBook(long bookId, int mark) {
        Book book = getBookById(bookId);
        if (book != null) {
                List<Integer> marks = book.getMarks();
                marks.add(mark);
                book.setMarks(marks);
                bookRepository.save(book);
                log.info("Оценка {} добавлена в список оценок книги с id {}", mark, bookId);
        }
    }

    /**
     * Вычисляет рейтинг книги с указанным идентификатором.
     * Если книга с таким идентификатором не найдена или у неё нет оценок, возвращает 0.
     *
     * @param bookId идентификатор книги
     * @return рейтинг книги или 0, если книга не найдена или у неё нет оценок
     */
    public double calculateBookRating(long bookId) {
        double result = 0;
        Book book = getBookById(bookId);
        if (book != null) {
            List<Integer> marks = book.getMarks();
            if (marks.isEmpty()) {
                return 0;
            }
            double sum = 0;
            for (int mark : marks) {
                sum += mark;
            }
            result = sum / marks.size();
        }
        return result;
    }
}