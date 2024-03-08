/*package com.spring.vsurin.bookexchange;

import com.spring.vsurin.bookexchange.app.BookService;
import com.spring.vsurin.bookexchange.app.UserService;
import com.spring.vsurin.bookexchange.domain.Book;
import com.spring.vsurin.bookexchange.domain.BookGenre;
import com.spring.vsurin.bookexchange.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        Book savedBook = bookService.createBook(book);
        assertNotNull(savedBook.getId());
    }

    @Test
    public void testGetBookById() {
        Book book = new Book();
        book.setTitle("testBook");
        book.setAuthor("testBookAuthor");
        Book savedBook = bookService.createBook(book);
        long bookId = savedBook.getId();
        Book retrievedBook = bookService.getBookById(bookId);
        assertNotNull(retrievedBook);
        assertEquals(bookId, retrievedBook.getId());
    }

    @Test
    public void testGetAllBooksInBase() {
        Book book1 = new Book();
        book1.setTitle("testBook");
        book1.setAuthor("testBookAuthor");
        Book savedBook1 = bookService.createBook(book1);

        Book book2 = new Book();
        book2.setTitle("testBook2");
        book2.setAuthor("testBookAuthor2");
        Book savedBook2 = bookService.createBook(book2);

        List<Book> result = new ArrayList<>();
        result.add(book1);
        result.add(book2);
        assertEquals(result, bookService.getAllBooksInBase());
    }

    @Test
    public void testSearchByGenre() {
        Book book1 = new Book();
        book1.setTitle("testBook");
        book1.setAuthor("testBookAuthor");
        book1.setGenre(BookGenre.ADVENTURE);
        Book savedBook1 = bookService.createBook(book1);

        Book book2 = new Book();
        book2.setTitle("testBook2");
        book2.setAuthor("testBookAuthor2");
        book2.setGenre(BookGenre.FICTION);
        Book savedBook2 = bookService.createBook(book2);

        Book book3 = new Book();
        book3.setTitle("testBook3");
        book3.setAuthor("testBookAuthor3");
        book1.setGenre(BookGenre.ART);
        Book savedBook3 = bookService.createBook(book3);

        BookGenre genreToSearch = BookGenre.FICTION;
        List<Book> foundBooks = bookService.searchByGenre(genreToSearch);
        List<Book> expectedBooks = new ArrayList<>();
        expectedBooks.add(book2);

        assertEquals(expectedBooks, foundBooks);
    }

    @Test
    public void testSearchByTitleOrAuthor() {
        Book book1 = new Book();
        book1.setTitle("testBook");
        book1.setAuthor("testBookAuthor");
        book1.setGenre(BookGenre.ADVENTURE);
        Book savedBook1 = bookService.createBook(book1);

        Book book2 = new Book();
        book2.setTitle("testBook2");
        book2.setAuthor("testBookAuthor2");
        book2.setGenre(BookGenre.FICTION);
        Book savedBook2 = bookService.createBook(book2);

        Book book3 = new Book();
        book3.setTitle("testBook3");
        book3.setAuthor("testBookAuthor3");
        book1.setGenre(BookGenre.ART);
        Book savedBook3 = bookService.createBook(book3);

        String searchRequest = "testBook2";
        List<Book> foundBooks = bookService.searchByTitleOrAuthor(searchRequest);
        List<Book> expectedBooks = new ArrayList<>();
        expectedBooks.add(book2);

        assertEquals(expectedBooks, foundBooks);
    }

    @Test
    public void testGetAvailableForExchanheBooks() {
        Book book1 = new Book();
        book1.setTitle("testBook");
        book1.setAuthor("testBookAuthor");
        book1.setGenre(BookGenre.ADVENTURE);
        Book savedBook1 = bookService.createBook(book1);

        Book book2 = new Book();
        book2.setTitle("testBook2");
        book2.setAuthor("testBookAuthor2");
        book2.setGenre(BookGenre.FICTION);
        Book savedBook2 = bookService.createBook(book2);

        User user1 = new User();
        user1.setUsername("testUser");
        user1.setEmail("test@example.com");

        User user2 = new User();
        user2.setUsername("testUser2");
        user2.setEmail("test2@example.com");

        userService.addBookToUserLibrary(user1.getId(), book1.getId());
        userService.addBookToUserLibrary(user1.getId(), book2.getId());
        userService.addBookToUserLibrary(user2.getId(), book1.getId());

        userService.addBookToOfferedByUser(user1.getId(), book1.getId());
        userService.addBookToOfferedByUser(user2.getId(), book1.getId());

        List<Book> expectedBooks = new ArrayList<>();
        expectedBooks.add(book1);

        assertEquals(expectedBooks, bookService.getAvailableForExchanheBooks());
    }

    @Test
    public void testAddMarkToBook() {
        Book book1 = new Book();
        book1.setTitle("testBook");
        book1.setAuthor("testBookAuthor");
        book1.setGenre(BookGenre.ADVENTURE);
        Book savedBook1 = bookService.createBook(book1);

        bookService.addMarkToBook(book1.getId(), 5);
        bookService.addMarkToBook(book1.getId(), 7);

        List<Integer> expectedMarks = new ArrayList<>();
        expectedMarks.add(5);
        expectedMarks.add(7);

        assertEquals(expectedMarks, book1.getMarks());
    }

    @Test
    public void testCalculateBookRating() {
        Book book1 = new Book();
        book1.setTitle("testBook");
        book1.setAuthor("testBookAuthor");
        book1.setGenre(BookGenre.ADVENTURE);
        Book savedBook1 = bookService.createBook(book1);

        bookService.addMarkToBook(book1.getId(), 5);
        bookService.addMarkToBook(book1.getId(), 7);

        int expectedResult = 6;

        assertEquals(expectedResult, bookService.calculateBookRating(book1.getId()));
    }
}*/