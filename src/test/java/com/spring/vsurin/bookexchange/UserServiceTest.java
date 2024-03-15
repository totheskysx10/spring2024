package com.spring.vsurin.bookexchange;

import com.spring.vsurin.bookexchange.app.BookService;
import com.spring.vsurin.bookexchange.app.ExchangeService;
import com.spring.vsurin.bookexchange.app.UserService;
import com.spring.vsurin.bookexchange.domain.Exchange;
import com.spring.vsurin.bookexchange.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private ExchangeService exchangeService;

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        user.setPhoneNumber("8915461564");
        User savedUser = userService.createUser(user);
        assertNotNull(savedUser.getId());
    }

    @Sql("/no_exchanges.sql")
    @Test
    public void testGetUserById() {
        User retrievedUser = userService.getUserById(1);
        assertNotNull(retrievedUser);
        assertEquals(1, retrievedUser.getId());
    }

    @Sql("/no_exchanges.sql")
    @Test
    public void testGetUserByIdNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            userService.getUserById(4);
        });
    }

    @Sql("/no_exchanges.sql")
    @Test
    public void testDeleteUser() {
        userService.deleteUser(1);
        assertNull(userService.getUserById(1));
    }

    @Sql("/no_exchanges.sql")
    @Test
    public void testAddBookToUser() {
            userService.addBookToUserLibrary(1, 1);

            User updatedUser = userService.getUserById(1);

            assertNotNull(updatedUser);
            assertEquals(1, updatedUser.getLibrary().size());
    }
    @Sql("/no_exchanges.sql")
    @Test
    public void testRemoveBookFromUser() {

        userService.addBookToUserLibrary(1, 1);
        userService.removeBookFromUserLibrary(1, 1);

        User updatedUser = userService.getUserById(1);
        assertNotNull(updatedUser);
        assertEquals(0, updatedUser.getLibrary().size());
    }

    @Sql("/no_exchanges.sql")
    @Test
    public void testAddBookToOfferedByUser() {

        userService.addBookToUserLibrary(1, 1);
        userService.addBookToOfferedByUser(1, 1);

        User updatedUser = userService.getUserById(1);

        assertNotNull(updatedUser);
        assertEquals(1, updatedUser.getOfferedBooks().size());
    }

    @Sql("/no_exchanges.sql")
    @Test
    public void testAddBookInExchangeToOfferedByUser() {

        userService.addBookToUserLibrary(1, 1);
        userService.addBookToUserLibrary(1, 3);
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


        userService.addBookToOfferedByUser(1, 1);

        User updatedUser = userService.getUserById(1);

        assertNotNull(updatedUser);
        assertEquals(0, updatedUser.getOfferedBooks().size());
    }

    @Sql("/no_exchanges.sql")
    @Test
    public void testRemoveBookFromOfferedByUser() {

        userService.addBookToUserLibrary(1, 1);
        userService.addBookToOfferedByUser(1, 1);
        userService.removeBookFromOfferedByUser(1, 1);

        User updatedUser = userService.getUserById(1);
        assertNotNull(updatedUser);
        assertEquals(0, updatedUser.getOfferedBooks().size());
    }

    @Sql("/no_exchanges.sql")
    @Test
    public void testAddAddressToUser() {

        String address = "Test";

        userService.addAddressToUser(1, address);

        User updatedUser = userService.getUserById(1);
        assertNotNull(updatedUser);
        assertEquals(1, updatedUser.getAddressList().size());
    }

    @Sql("/no_exchanges.sql")
    @Test
    public void testRemoveAddressFromUser() {

        String address1 = "Test";
        String address2 = "Test";

        userService.addAddressToUser(1, address1);
        userService.addAddressToUser(1, address2);
        userService.removeAddressFromUser(1, address1);

        User updatedUser = userService.getUserById(1);
        assertNotNull(updatedUser);
        assertEquals(1, updatedUser.getAddressList().size());
    }

    @Sql("/no_exchanges.sql")
    @Test
    public void testUpdateUserPhone() {

        String phone = "89123456789";

        userService.updateUserPhone(1, phone);

        User updatedUser = userService.getUserById(1);
        assertNotNull(updatedUser);
        assertEquals("89123456789", updatedUser.getPhoneNumber());
    }

    @Sql("/no_exchanges.sql")
    @Test
    public void testUpdateUserMail() {

        String email = "new@example.com";

        userService.updateUserMail(1, email);

        User updatedUser = userService.getUserById(1);
        assertNotNull(updatedUser);
        assertEquals("new@example.com", updatedUser.getEmail());
    }

    @Sql("/no_exchanges.sql")
    @Test
    public void testUpdateMainAddress() {

        String address1 = "Test";
        String address2 = "Test2";

        userService.addAddressToUser(1, address1);
        userService.addAddressToUser(1, address2);
        userService.updateMainAddress(1, 1);

        User updatedUser = userService.getUserById(1);
        assertNotNull(updatedUser);
        assertEquals("Test2", updatedUser.getMainAddress());
    }

    @Sql("/no_exchanges.sql")
    @Test
    public void testGetAllUserExchanges() {

        Exchange ex1 = new Exchange();
        ex1.setMember1(userService.getUserById(1));
        ex1.setMember2(userService.getUserById(2));
        ex1.setExchangedBook1(bookService.getBookById(1));
        ex1.setExchangedBook2(bookService.getBookById(2));
        Exchange savedEx1 = exchangeService.createExchange(ex1);

        Exchange ex2 = new Exchange();
        ex2.setMember1(userService.getUserById(2));
        ex2.setMember2(userService.getUserById(1));
        ex2.setExchangedBook1(bookService.getBookById(1));
        ex2.setExchangedBook2(bookService.getBookById(2));
        Exchange savedEx2 = exchangeService.createExchange(ex2);

        User updUser2 = userService.getUserById(2);

        assertEquals(2, userService.getAllUserExchanges(updUser2.getId()).size());
    }
}