package com.spring.vsurin.bookexchange;

import com.spring.vsurin.bookexchange.app.BookService;
import com.spring.vsurin.bookexchange.app.UserService;
import com.spring.vsurin.bookexchange.domain.Book;
import com.spring.vsurin.bookexchange.domain.BookGenre;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.time.Year;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @Test
    public void testCreateBook() {
        Book book = new Book();
        book.setTitle("testBook");
        book.setAuthor("testBookAuthor");
        book.setDescription("123desc");
        book.setGenre(BookGenre.FICTION);
        book.setIsbn("123");
        book.setPublicationYear(Year.of(2005));
        Book savedBook = bookService.createBook(book);
        assertNotNull(savedBook.getId());
    }

    @Sql("/no_exchanges.sql")
    @Test
    public void testGetBookById() {
        Book retrievedBook = bookService.getBookById(1);
        assertNotNull(retrievedBook);
        assertEquals(1, retrievedBook.getId());
    }

    @Sql("/no_exchanges.sql")
    @Test
    public void testGetBookByIdNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            bookService.getBookById(4);
        });
    }
    @Sql("/no_exchanges.sql")
    @Test
    public void testGetAllBooksInBase() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> booksPage = bookService.getAllBooksInBase(pageable);
        List<Book> books = booksPage.getContent();
        assertEquals(3, books.size());
    }

    @Sql("/no_exchanges.sql")
    @Test
    public void testSearchByGenre() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> booksPage = bookService.searchByGenre(BookGenre.FICTION, pageable);
        List<Book> books = booksPage.getContent();

        assertEquals(1, books.size());
    }

    @Sql("/no_exchanges.sql")
    @Test
    public void testSearchByTitleOrAuthor() {
        Pageable pageable = PageRequest.of(0, 10);
        String searchRequest = "Test Bo";
        Page<Book> booksPage = bookService.searchByTitleOrAuthor(searchRequest, pageable);
        List<Book> books = booksPage.getContent();

        assertEquals(3, books.size());
    }

    @Sql("/no_exchanges.sql")
    @Test
    public void testGetAvailableForExchangeBooks() {
        userService.addBookToUserLibrary(1, 1);
        userService.addBookToUserLibrary(2, 1);
        userService.addBookToUserLibrary(2, 2);

        userService.addBookToOfferedByUser(1, 1);
        userService.addBookToOfferedByUser(2, 1);

        Page<Book> booksPage = bookService.getAvailableForExchangeBooks(0, 10);
        List<Book> books = booksPage.getContent();

        assertEquals(1, books.size());
    }

    @Sql("/no_exchanges.sql")
    @Test
    public void testAddMarkToBook() {
        bookService.addMarkToBook(1, 5);
        bookService.addMarkToBook(1, 7);

        Book updatedBook = bookService.getBookById(1);

        assertEquals(2, updatedBook.getMarks().size());
    }

    @Sql("/no_exchanges.sql")
    @Test
    public void testUpdateDescriptionToBook() {
        bookService.updateDescriptionToBook(1, "DEEEESC");

        Book updatedBook = bookService.getBookById(1);

        assertEquals("DEEEESC", updatedBook.getDescription());
    }
}