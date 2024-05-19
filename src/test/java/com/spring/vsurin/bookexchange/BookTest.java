package com.spring.vsurin.bookexchange;

import com.spring.vsurin.bookexchange.domain.Book;
import com.spring.vsurin.bookexchange.domain.BookGenre;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Year;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class BookTest {

    @Test
    public void testAddMarkToBookAndCalculateBookRating() {
        byte[] image = new byte[1];
        Book book = new Book(1, new ArrayList<>(), "Title", "Author", Year.of(2005), "123", BookGenre.FICTION, "DESC", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), image, 0);
        book.addMarkToBook(5);
        book.addMarkToBook(7);

        int expectedResult = 6;

        assertEquals(expectedResult, book.calculateBookRating());
    }
}
