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

@Sql("/no_exchanges.sql")
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
        userService.deleteUser(1);
        userService.deleteUser(2);
        userService.deleteUser(3);

        User user = User.builder()
                .username("testUser")
                .email("test@example.com")
                .phoneNumber("8915461564")
                .mainAddress("")
                .build();

        User savedUser = userService.createUser(user);
        assertNotNull(savedUser.getId());
    }

    @Test
    public void testGetUserById() {
        User retrievedUser = userService.getUserById(1);
        assertNotNull(retrievedUser);
        assertEquals(1, retrievedUser.getId());
    }

    @Test
    public void testGetUserByIdNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            userService.getUserById(4);
        });
    }

    @Test
    public void testDeleteUser() {
        userService.deleteUser(1);
        assertThrows(IllegalArgumentException.class, () -> {
            userService.getUserById(1);
        });
    }

    @Test
    public void testAddBookToUser() {
        userService.addBookToUserLibrary(1, 1);

        User updatedUser = userService.getUserById(1);

        assertNotNull(updatedUser);
        assertEquals(1, updatedUser.getLibrary().size());
    }

    @Test
    public void testRemoveBookFromUser() {
        userService.addBookToUserLibrary(1, 1);
        userService.removeBookFromUserLibrary(1, 1);

        User updatedUser = userService.getUserById(1);
        assertNotNull(updatedUser);
        assertEquals(0, updatedUser.getLibrary().size());
    }

    @Test
    public void testAddBookToOfferedByUser() {
        userService.addBookToUserLibrary(1, 1);
        userService.addBookToOfferedByUser(1, 1);

        User updatedUser = userService.getUserById(1);

        assertNotNull(updatedUser);
        assertEquals(1, updatedUser.getOfferedBooks().size());
    }
    @Sql("/with_exchanges.sql")
    @Test
    public void testAddBookInExchangeToOfferedByUser() {
        exchangeService.setNoTrack(1, 1);
        exchangeService.setNoTrack(2, 1);

        userService.addBookToOfferedByUser(1, 1);

        User updatedUser = userService.getUserById(1);

        assertNotNull(updatedUser);
        assertEquals(0, updatedUser.getOfferedBooks().size());
    }

    @Test
    public void testRemoveBookFromOfferedByUser() {
        userService.addBookToUserLibrary(1, 1);
        userService.addBookToOfferedByUser(1, 1);
        userService.removeBookFromOfferedByUser(1, 1);

        User updatedUser = userService.getUserById(1);
        assertNotNull(updatedUser);
        assertEquals(0, updatedUser.getOfferedBooks().size());
    }

    @Test
    public void testAddAddressToUser() {
        String address = "Test";

        userService.addAddressToUser(1, address);

        User updatedUser = userService.getUserById(1);
        assertNotNull(updatedUser);
        assertEquals(1, updatedUser.getAddressList().size());
    }

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

    @Test
    public void testUpdateUserPhone() {
        String phone = "89123456789";

        userService.updateUserPhone(1, phone);

        User updatedUser = userService.getUserById(1);
        assertNotNull(updatedUser);
        assertEquals("89123456789", updatedUser.getPhoneNumber());
    }

    @Test
    public void testUpdateUserMail() {
        String email = "new@example.com";

        userService.updateUserMail(1, email);

        User updatedUser = userService.getUserById(1);
        assertNotNull(updatedUser);
        assertEquals("new@example.com", updatedUser.getEmail());
    }

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
    @Sql("/with_exchanges.sql")
    @Test
    public void testGetAllUserExchanges() {
        User updUser2 = userService.getUserById(1);

        assertEquals(2, userService.getAllUserExchanges(updUser2.getId()).size());
    }
}