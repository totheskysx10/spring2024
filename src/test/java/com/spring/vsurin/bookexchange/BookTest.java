package com.spring.vsurin.bookexchange;

import com.spring.vsurin.bookexchange.app.BookService;
import com.spring.vsurin.bookexchange.domain.Book;
import com.spring.vsurin.bookexchange.domain.BookGenre;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.Year;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class BookTest {

    @Test
    public void testCalculateBookRating() {
        Book book = new Book(1, new ArrayList<>(), "Title", "Author", Year.of(2005), "123", BookGenre.FICTION, "DESC", new ArrayList<>(), new ArrayList<>());
        book.getMarks().add(5);
        book.getMarks().add(7);

        int expectedResult = 6;

        assertEquals(expectedResult, book.calculateBookRating());
    }
}
