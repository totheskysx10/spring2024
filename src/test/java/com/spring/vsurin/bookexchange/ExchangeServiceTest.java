package com.spring.vsurin.bookexchange;

import com.spring.vsurin.bookexchange.app.BookService;
import com.spring.vsurin.bookexchange.app.ExchangeService;
import com.spring.vsurin.bookexchange.app.UserService;
import com.spring.vsurin.bookexchange.domain.Exchange;
import com.spring.vsurin.bookexchange.domain.ExchangeStatus;
import com.spring.vsurin.bookexchange.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ExchangeServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private ExchangeService exchangeService;

    @Sql("/no_exchanges.sql")
    @Test
    public void testCreateExchange() {
        Exchange ex1 = new Exchange();
        ex1.setMember1(userService.getUserById(1));
        ex1.setMember2(userService.getUserById(2));
        ex1.setExchangedBook1(bookService.getBookById(1));
        ex1.setExchangedBook2(bookService.getBookById(2));
        Exchange savedEx1 = exchangeService.createExchange(ex1);
        assertNotNull(savedEx1.getId());
    }

    @Sql("/no_exchanges.sql")
    @Test
    public void testGetExchangeById() {
        Exchange ex1 = new Exchange();
        ex1.setMember1(userService.getUserById(1));
        ex1.setMember2(userService.getUserById(2));
        ex1.setExchangedBook1(bookService.getBookById(1));
        ex1.setExchangedBook2(bookService.getBookById(2));
        Exchange savedEx1 = exchangeService.createExchange(ex1);

        long exchangeId = savedEx1.getId();
        Exchange retrievedEx = exchangeService.getExchangeById(exchangeId);
        assertNotNull(retrievedEx);
        assertEquals(exchangeId, retrievedEx.getId());
    }

    @Sql("/no_exchanges.sql")
    @Test
    public void testGetExchangeByIdNull() {
        Exchange ex1 = new Exchange();
        ex1.setMember1(userService.getUserById(1));
        ex1.setMember2(userService.getUserById(2));
        ex1.setExchangedBook1(bookService.getBookById(1));
        ex1.setExchangedBook2(bookService.getBookById(2));
        Exchange savedEx1 = exchangeService.createExchange(ex1);

        assertThrows(IllegalArgumentException.class, () -> {
            exchangeService.getExchangeById(2);
        });
    }

    @Sql("/no_exchanges.sql")
    @Test
    public void testUdpateTrackSetByUser() {
        Exchange ex1 = new Exchange();
        ex1.setMember1(userService.getUserById(1));
        ex1.setMember2(userService.getUserById(2));
        ex1.setExchangedBook1(bookService.getBookById(1));
        ex1.setExchangedBook2(bookService.getBookById(2));
        Exchange savedEx1 = exchangeService.createExchange(ex1);

        exchangeService.udpateTrackSetByUser(1, savedEx1.getId(), "123");
        exchangeService.udpateTrackSetByUser(2, savedEx1.getId(), "1234");
        Exchange updEx = exchangeService.getExchangeById(ex1.getId());

        assertEquals("123", updEx.getTrack1());
        assertEquals("1234", updEx.getTrack2());
    }

    @Sql("/no_exchanges.sql")
    @Test
    public void testSetNoTrack() {
        Exchange ex1 = new Exchange();
        ex1.setMember1(userService.getUserById(1));
        ex1.setMember2(userService.getUserById(2));
        ex1.setExchangedBook1(bookService.getBookById(1));
        ex1.setExchangedBook2(bookService.getBookById(2));
        Exchange savedEx1 = exchangeService.createExchange(ex1);

        exchangeService.setNoTrack(1, savedEx1.getId());
        exchangeService.setNoTrack(2, savedEx1.getId());
        Exchange updEx = exchangeService.getExchangeById(ex1.getId());

        assertEquals("DELIVERY_WITHOUT_TRACK", updEx.getTrack1());
        assertEquals("DELIVERY_WITHOUT_TRACK", updEx.getTrack2());
    }

    @Sql("/no_exchanges.sql")
    @Test
    public void testSearchByStatusAndMember() {
        Exchange ex1 = new Exchange();
        ex1.setMember1(userService.getUserById(1));
        ex1.setMember2(userService.getUserById(2));
        ex1.setExchangedBook1(bookService.getBookById(1));
        ex1.setExchangedBook2(bookService.getBookById(2));
        Exchange savedEx1 = exchangeService.createExchange(ex1);

        exchangeService.setNoTrack(1, savedEx1.getId());
        exchangeService.setNoTrack(2, savedEx1.getId());
        Exchange updEx = exchangeService.getExchangeById(ex1.getId());

        Exchange ex2 = new Exchange();
        ex2.setMember1(userService.getUserById(1));
        ex2.setMember2(userService.getUserById(2));
        ex2.setExchangedBook1(bookService.getBookById(2));
        ex2.setExchangedBook2(bookService.getBookById(3));
        Exchange savedEx2 = exchangeService.createExchange(ex2);

        exchangeService.setNoTrack(3, savedEx2.getId());
        exchangeService.setNoTrack(2, savedEx2.getId());
        Exchange updEx2 = exchangeService.getExchangeById(ex2.getId());

        List<Exchange> result = exchangeService.searchByStatusAndMember(ExchangeStatus.IN_PROGRESS, 1);

        assertEquals(1, result.size());
    }

    @Sql("/no_exchanges.sql")
    @Test
    public void testReceiveBooks() {
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
        exchangeService.setNoTrack(1, savedEx1.getId());
        exchangeService.setNoTrack(2, savedEx1.getId());

        exchangeService.receiveBook(ex1.getId(), 1);
        exchangeService.receiveBook(ex1.getId(), 2);

        User updatedUser = userService.getUserById(1);
        User updatedUser2 = userService.getUserById(2);

        assertEquals(ExchangeStatus.COMPLETED, exchangeService.getExchangeById(ex1.getId()).getStatus());
        assertEquals(2, updatedUser.getLibrary().get(0).getId());
        assertEquals(1, updatedUser2.getLibrary().get(0).getId());
    }

    @Sql("/no_exchanges.sql")
    @Test
    public void testSetProblemsStatus() {
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
        exchangeService.setNoTrack(1, savedEx1.getId());
        exchangeService.setNoTrack(2, savedEx1.getId());

        assertThrows(IllegalStateException.class, () -> {
            exchangeService.setProblemsStatus(ex1.getId());
        });
    }
}
