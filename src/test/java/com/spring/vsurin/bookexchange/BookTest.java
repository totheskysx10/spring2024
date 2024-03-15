package com.spring.vsurin.bookexchange;

import com.spring.vsurin.bookexchange.app.BookService;
import com.spring.vsurin.bookexchange.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class BookTest {
    @Autowired
    private BookService bookService;

    @Sql("/no_exchanges.sql")
    @Test
    public void testCalculateBookRating() {
        bookService.addMarkToBook(1, 5);
        bookService.addMarkToBook(1, 7);

        int expectedResult = 6;

        assertEquals(expectedResult, bookService.getBookById(1).calculateBookRating());
    }
}
