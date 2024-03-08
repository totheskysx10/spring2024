/*package com.spring.vsurin.bookexchange;

import com.spring.vsurin.bookexchange.app.BookService;
import com.spring.vsurin.bookexchange.app.UserService;
import com.spring.vsurin.bookexchange.domain.Book;
import com.spring.vsurin.bookexchange.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Test
    public void testCreateUser() {
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        User savedUser = userService.createUser(user);
        assertNotNull(savedUser.getId());
    }

    @Test
    public void testGetUserById() {
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        User savedUser = userService.createUser(user);
        long userId = savedUser.getId();
        User retrievedUser = userService.getUserById(userId);
        assertNotNull(retrievedUser);
        assertEquals(userId, retrievedUser.getId());
    }

    @Test
    public void testDeleteUser() {
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        User savedUser = userService.createUser(user);
        long userId = savedUser.getId();
        userService.deleteUser(userId);
        assertNull(userService.getUserById(userId));
    }

    @Test
    public void testAddBookToUser() {
            User user = new User();
            user.setUsername("testUser");
            user.setEmail("test@example.com");
            User savedUser = userService.createUser(user);;
            long userId = savedUser.getId();

            Book book = new Book();
            book.setTitle("Test Book");
            book.setAuthor("Test Author");
            Book savedBook = bookService.createBook(book);
            long bookId = savedBook.getId();

            userService.addBookToUserLibrary(userId, bookId);

            User updatedUser = userService.getUserById(userId);
            assertNotNull(updatedUser);
            assertEquals(1, updatedUser.getLibrary().size());
    }

    @Test
    public void testRemoveBookFromUser() {
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        User savedUser = userService.createUser(user);
        long userId = savedUser.getId();

        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        Book savedBook = bookService.createBook(book);
        long bookId = savedBook.getId();

        userService.addBookToUserLibrary(userId, bookId);
        userService.removeBookFromUserLibrary(userId, bookId);

        User updatedUser = userService.getUserById(userId);
        assertNotNull(updatedUser);
        assertEquals(0, updatedUser.getLibrary().size());
    }

    @Test
    public void testAddBookToOfferedByUser() {
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        User savedUser = userService.createUser(user);
        long userId = savedUser.getId();

        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        Book savedBook = bookService.createBook(book);
        long bookId = savedBook.getId();

        userService.addBookToUserLibrary(userId, bookId);
        userService.addBookToOfferedByUser(userId, bookId);

        User updatedUser = userService.getUserById(userId);
        assertNotNull(updatedUser);
        assertEquals(1, updatedUser.getOfferedBooks().size());
    }

    @Test
    public void testRemoveBookFromOfferedByUser() {
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        User savedUser = userService.createUser(user);
        long userId = savedUser.getId();

        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        Book savedBook = bookService.createBook(book);
        long bookId = savedBook.getId();

        userService.addBookToUserLibrary(userId, bookId);
        userService.addBookToOfferedByUser(userId, bookId);
        userService.removeBookFromOfferedByUser(userId, bookId);

        User updatedUser = userService.getUserById(userId);
        assertNotNull(updatedUser);
        assertEquals(0, updatedUser.getOfferedBooks().size());
    }

    @Test
    public void testAddAddressToUser() {
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        User savedUser = userService.createUser(user);
        long userId = savedUser.getId();

        String address = "Test";

        userService.addAddressToUser(userId, address);

        User updatedUser = userService.getUserById(userId);
        assertNotNull(updatedUser);
        assertEquals(1, updatedUser.getAddressList().size());
    }

    @Test
    public void testRemoveAddressFromUser() {
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        User savedUser = userService.createUser(user);
        long userId = savedUser.getId();

        String address = "Test";

        userService.addAddressToUser(userId, address);
        userService.removeAddressFromUser(userId, address);

        User updatedUser = userService.getUserById(userId);
        assertNotNull(updatedUser);
        assertEquals(0, updatedUser.getAddressList().size());
    }

    @Test
    public void testUpdateUserPhone() {
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        user.setPhoneNumber("89000000000");
        User savedUser = userService.createUser(user);
        long userId = savedUser.getId();

        String phone = "89123456789";

        userService.updateUserPhone(userId, phone);

        User updatedUser = userService.getUserById(userId);
        assertNotNull(updatedUser);
        assertEquals("89123456789", updatedUser.getPhoneNumber());
    }

    @Test
    public void testUpdateUserMail() {
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        User savedUser = userService.createUser(user);
        long userId = savedUser.getId();

        String email = "new@example.com";

        userService.updateUserMail(userId, email);

        User updatedUser = userService.getUserById(userId);
        assertNotNull(updatedUser);
        assertEquals("new@example.com", updatedUser.getEmail());
    }
}*/