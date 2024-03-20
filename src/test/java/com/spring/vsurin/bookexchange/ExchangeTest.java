package com.spring.vsurin.bookexchange;

import com.spring.vsurin.bookexchange.app.BookService;
import com.spring.vsurin.bookexchange.app.ExchangeService;
import com.spring.vsurin.bookexchange.app.UserService;
import com.spring.vsurin.bookexchange.domain.Exchange;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ExchangeTest {
    @Autowired
    private ExchangeService exchangeService;
    @Autowired
    private UserService userService;
    @Autowired
    private BookService bookService;

    @Sql("/no_exchanges.sql")
    @Test
    public void testSetCurrentDate() {
        userService.addBookToUserLibrary(1, 1);
        userService.addBookToUserLibrary(2, 2);
        userService.addBookToOfferedByUser(1, 1);
        userService.addBookToOfferedByUser(2, 2);

        Exchange ex1 = new Exchange();
        ex1.setMember1(userService.getUserById(1));
        ex1.setMember2(userService.getUserById(2));
        ex1.setExchangedBook1(bookService.getBookById(1));
        ex1.setExchangedBook2(bookService.getBookById(2));
        Exchange savedEx1 = exchangeService.createExchange(ex1);

        assertEquals(LocalDate.now(), ex1.getDate());
    }
}
