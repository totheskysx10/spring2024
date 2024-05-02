package com.spring.vsurin.bookexchange;

import com.spring.vsurin.bookexchange.app.*;
import com.spring.vsurin.bookexchange.domain.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DataJpaTest
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ExchangeService exchangeService;

    @Mock
    private BookService bookService;

    @Mock
    private EmailService emailService;

    @Test
    public void testGetUserById() {
        User user = User.builder()
                .id(1)
                .email("min0@list.ru")
                .username("us")
                .role(UserRole.ROLE_USER)
                .gender(UserGender.MALE)
                .build();

        when(userRepository.findById(1)).thenReturn(user);

        User retrievedUser = userService.getUserById(1);
        assertNotNull(retrievedUser);
        assertEquals(1, retrievedUser.getId());
    }

    @Test
    public void testGetUserByIdNull() {
        User user = User.builder()
                .id(1)
                .email("min0@list.ru")
                .username("us")
                .role(UserRole.ROLE_USER)
                .gender(UserGender.MALE)
                .build();

        when(userRepository.findById(1)).thenReturn(user);

        assertThrows(IllegalArgumentException.class, () -> {
            userService.getUserById(4);
        });
    }

    @Test
    public void testDeleteUser() {
        User user = User.builder()
                .id(1)
                .email("min095@list.ru")
                .username("test")
                .role(UserRole.ROLE_ADMIN)
                .gender(UserGender.MALE)
                .build();

        when(userRepository.findById(1)).thenReturn(user);

        userService.deleteUser(1);

        verify(emailService).sendEmail(eq("min095@list.ru"), anyString(), anyString());

        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    public void testSendRequestToDeleteUser() {
        User admin1 = User.builder()
                .id(1)
                .email("min095@list.ru")
                .username("ad")
                .role(UserRole.ROLE_ADMIN)
                .gender(UserGender.MALE)
                .build();

        User admin2 = User.builder()
                .id(2)
                .email("min095@list.ru")
                .username("ad")
                .role(UserRole.ROLE_ADMIN)
                .gender(UserGender.MALE)
                .build();

        List<User> admins = new ArrayList<>();
        admins.add(admin1);
        admins.add(admin2);

        User user = User.builder()
                .id(3)
                .email("min0@list.ru")
                .username("us")
                .role(UserRole.ROLE_USER)
                .gender(UserGender.MALE)
                .build();

        when(userRepository.findByRole(UserRole.ROLE_ADMIN)).thenReturn(admins);
        when(userRepository.findById(3)).thenReturn(user);

        userService.sendRequestToDeleteUser(3, "anyReason");

        verify(emailService, times(2)).sendEmail(eq("min095@list.ru"), anyString(), anyString());
    }

    @Test
    public void testAddBookToUser() {
        User user = User.builder()
                .id(1)
                .email("min0@list.ru")
                .username("us")
                .role(UserRole.ROLE_USER)
                .gender(UserGender.MALE)
                .library(new ArrayList<>())
                .build();

        Book testBook1 = Book.builder()
                .id(1)
                .title("Test Book 4")
                .author("Test Author 4")
                .description("Test description")
                .genre(BookGenre.ART)
                .isbn("101")
                .usersOfferingForExchange(new ArrayList<>())
                .publicationYear(Year.of(2010))
                .build();

        when(userRepository.findById(1)).thenReturn(user);
        when(bookService.getBookById(1)).thenReturn(testBook1);

        userService.addBookToUserLibrary(1, 1);

        User updatedUser = userService.getUserById(1);

        assertNotNull(updatedUser);
        assertEquals(1, updatedUser.getLibrary().size());
    }

    @Test
    public void testRemoveBookFromUser() {
        User user = User.builder()
                .id(1)
                .email("min0@list.ru")
                .username("us")
                .role(UserRole.ROLE_USER)
                .gender(UserGender.MALE)
                .library(new ArrayList<>())
                .build();

        Book testBook1 = Book.builder()
                .id(1)
                .title("Test Book 4")
                .author("Test Author 4")
                .description("Test description")
                .genre(BookGenre.ART)
                .isbn("101")
                .usersOfferingForExchange(new ArrayList<>())
                .publicationYear(Year.of(2010))
                .build();

        when(userRepository.findById(1)).thenReturn(user);
        when(bookService.getBookById(1)).thenReturn(testBook1);

        userService.addBookToUserLibrary(1, 1);
        userService.removeBookFromUserLibrary(1, 1);

        User updatedUser = userService.getUserById(1);
        assertNotNull(updatedUser);
        assertEquals(0, updatedUser.getLibrary().size());
    }

    @Test
    public void testAddBookToOfferedByUser() {
        User user = User.builder()
                .id(1)
                .email("min0@list.ru")
                .username("us")
                .role(UserRole.ROLE_USER)
                .gender(UserGender.MALE)
                .addressList(new ArrayList<>())
                .library(new ArrayList<>())
                .offeredBooks(new ArrayList<>())
                .exchangesAsMember1(new ArrayList<>())
                .exchangesAsMember2(new ArrayList<>())
                .mainAddress("Add")
                .build();

        Book testBook1 = Book.builder()
                .id(1)
                .title("Test Book 4")
                .author("Test Author 4")
                .description("Test description")
                .genre(BookGenre.ART)
                .isbn("101")
                .usersOfferingForExchange(new ArrayList<>())
                .publicationYear(Year.of(2010))
                .build();

        when(userRepository.findById(1)).thenReturn(user);
        when(bookService.getBookById(1)).thenReturn(testBook1);

        userService.addBookToUserLibrary(1, 1);
        userService.addBookToOfferedByUser(1, 1);

        User updatedUser = userService.getUserById(1);

        assertNotNull(updatedUser);
        assertEquals(1, updatedUser.getOfferedBooks().size());
    }

    @Test
    public void testAddBookToOfferedByUserWithoutAddress() {
        User user = User.builder()
                .id(1)
                .email("min0@list.ru")
                .username("us")
                .role(UserRole.ROLE_USER)
                .gender(UserGender.MALE)
                .library(new ArrayList<>())
                .offeredBooks(new ArrayList<>())
                .build();

        Book testBook1 = Book.builder()
                .id(1)
                .title("Test Book 4")
                .author("Test Author 4")
                .description("Test description")
                .genre(BookGenre.ART)
                .isbn("101")
                .usersOfferingForExchange(new ArrayList<>())
                .publicationYear(Year.of(2010))
                .build();

        when(userRepository.findById(1)).thenReturn(user);
        when(bookService.getBookById(1)).thenReturn(testBook1);

        userService.addBookToUserLibrary(1, 1);
        userService.addBookToOfferedByUser(1, 1);

        User updatedUser = userService.getUserById(1);

        assertNotNull(updatedUser);
        assertEquals(0, updatedUser.getOfferedBooks().size());
    }

    @Test
    public void testAddBookInExchangeToOfferedByUser() {
        User user = User.builder()
                .id(1)
                .email("min0@list.ru")
                .username("us")
                .role(UserRole.ROLE_USER)
                .gender(UserGender.MALE)
                .addressList(new ArrayList<>())
                .library(new ArrayList<>())
                .offeredBooks(new ArrayList<>())
                .mainAddress("add")
                .exchangesAsMember1(new ArrayList<>())
                .exchangesAsMember2(new ArrayList<>())
                .build();

        User user2 = User.builder()
                .id(2)
                .email("min01@list.ru")
                .username("us")
                .role(UserRole.ROLE_USER)
                .gender(UserGender.MALE)
                .addressList(new ArrayList<>())
                .library(new ArrayList<>())
                .offeredBooks(new ArrayList<>())
                .mainAddress("add")
                .exchangesAsMember1(new ArrayList<>())
                .exchangesAsMember2(new ArrayList<>())
                .build();

        Book testBook1 = Book.builder()
                .id(1)
                .title("Test Book 4")
                .author("Test Author 4")
                .description("Test description")
                .genre(BookGenre.ART)
                .isbn("101")
                .usersOfferingForExchange(new ArrayList<>())
                .publicationYear(Year.of(2010))
                .build();

        Book testBook2 = Book.builder()
                .id(2)
                .title("Test Book")
                .author("Test Author")
                .description("Test description")
                .genre(BookGenre.ART)
                .isbn("101")
                .usersOfferingForExchange(new ArrayList<>())
                .publicationYear(Year.of(2010))
                .build();

        Exchange exchange = Exchange.builder()
                .member1(user)
                .member2(user2)
                .exchangedBook1(testBook1)
                .exchangedBook2(testBook2)
                .status(ExchangeStatus.IN_PROGRESS)
                .build();

        user.getExchangesAsMember1().add(exchange);
        user2.getExchangesAsMember2().add(exchange);

        when(userRepository.findById(1)).thenReturn(user);
        when(userRepository.findById(2)).thenReturn(user2);
        when(bookService.getBookById(1)).thenReturn(testBook1);
        when(bookService.getBookById(2)).thenReturn(testBook2);
        when((exchangeService.getExchangeById(1))).thenReturn(exchange);

        userService.addBookToUserLibrary(1, 1);
        userService.addBookToUserLibrary(2, 2);

        userService.addBookToOfferedByUser(1, 1);

        User updatedUser = userService.getUserById(1);

        assertNotNull(updatedUser);
        assertEquals(0, updatedUser.getOfferedBooks().size());
    }

    @Test
    public void testRemoveBookFromOfferedByUser() {
        User user = User.builder()
                .id(1)
                .email("min0@list.ru")
                .username("us")
                .role(UserRole.ROLE_USER)
                .gender(UserGender.MALE)
                .addressList(new ArrayList<>())
                .library(new ArrayList<>())
                .offeredBooks(new ArrayList<>())
                .build();

        Book testBook = Book.builder()
                .id(1)
                .title("Test Book 4")
                .author("Test Author 4")
                .description("Test description")
                .genre(BookGenre.ART)
                .isbn("101")
                .publicationYear(Year.of(2010))
                .build();

        when(userRepository.findById(1)).thenReturn(user);
        when(bookService.getBookById(1)).thenReturn(testBook);

        userService.addBookToUserLibrary(1, 1);
        userService.addBookToOfferedByUser(1, 1);
        userService.removeBookFromOfferedByUser(1, 1);

        User updatedUser = userService.getUserById(1);
        assertNotNull(updatedUser);
        assertEquals(0, updatedUser.getOfferedBooks().size());
    }

    @Test
    public void testAddAddressToUser() {
        User user = User.builder()
                .id(1)
                .email("min0@list.ru")
                .username("us")
                .role(UserRole.ROLE_USER)
                .gender(UserGender.MALE)
                .addressList(new ArrayList<>())
                .build();

        when(userRepository.findById(1)).thenReturn(user);

        String address = "Test";

        userService.addAddressToUser(1, address);

        User updatedUser = userService.getUserById(1);
        assertNotNull(updatedUser);
        assertEquals(1, updatedUser.getAddressList().size());
    }

    @Test
    public void testRemoveAddressFromUser() {
        User user = User.builder()
                .id(1)
                .email("min0@list.ru")
                .username("us")
                .role(UserRole.ROLE_USER)
                .gender(UserGender.MALE)
                .addressList(new ArrayList<>())
                .build();

        when(userRepository.findById(1)).thenReturn(user);

        String address1 = "Test";
        String address2 = "Test2";

        userService.addAddressToUser(1, address1);
        userService.addAddressToUser(1, address2);
        userService.removeAddressFromUser(1, address1);

        User updatedUser = userService.getUserById(1);
        assertNotNull(updatedUser);
        assertEquals(1, updatedUser.getAddressList().size());
    }

    @Test
    public void testUpdateMainAddress() {
        User user = User.builder()
                .id(1)
                .email("min0@list.ru")
                .username("us")
                .role(UserRole.ROLE_USER)
                .gender(UserGender.MALE)
                .addressList(new ArrayList<>())
                .build();

        when(userRepository.findById(1)).thenReturn(user);

        String address1 = "Test";
        String address2 = "Test2";

        userService.addAddressToUser(1, address1);
        userService.addAddressToUser(1, address2);
        userService.updateMainAddress(1, 1);

        User updatedUser = userService.getUserById(1);
        assertNotNull(updatedUser);
        assertEquals("Test2", updatedUser.getMainAddress());
    }

    @Test
    public void testGetAllUserExchanges() {
        User user = User.builder()
                .id(1)
                .email("min0@list.ru")
                .username("us")
                .role(UserRole.ROLE_USER)
                .gender(UserGender.MALE)
                .exchangesAsMember1(new ArrayList<>())
                .exchangesAsMember2(new ArrayList<>())
                .build();

        user.getExchangesAsMember1().add(new Exchange());
        user.getExchangesAsMember2().add(new Exchange());

        when(userRepository.findById(1)).thenReturn(user);

        User updUser2 = userService.getUserById(1);

        assertEquals(2, userService.getAllUserExchanges(updUser2.getId()).size());
    }

    @Test
    public void testSetAdminStatus() {
        User user = User.builder()
                .id(1)
                .email("min0@list.ru")
                .username("us")
                .role(UserRole.ROLE_USER)
                .gender(UserGender.MALE)
                .build();

        when(userRepository.findById(1)).thenReturn(user);

        userService.setAdminStatus(1);

        User updatedUser = userService.getUserById(1);
        assertNotNull(updatedUser);
        assertEquals(UserRole.ROLE_ADMIN, updatedUser.getRole());
    }

    @Test
    public void testSetAdminStatusToAdmin() {
        User user = User.builder()
                .id(2)
                .email("min0@list.ru")
                .username("us")
                .role(UserRole.ROLE_ADMIN)
                .gender(UserGender.MALE)
                .build();

        when(userRepository.findById(2)).thenReturn(user);

        userService.setAdminStatus(2);

        User updatedUser = userService.getUserById(2);
        assertNotNull(updatedUser);
        assertEquals(UserRole.ROLE_ADMIN, updatedUser.getRole());
    }

    @Test
    public void testRemoveAdminStatus() {
        User user = User.builder()
                .id(1)
                .email("min0@list.ru")
                .username("us")
                .role(UserRole.ROLE_ADMIN)
                .gender(UserGender.MALE)
                .build();

        when(userRepository.findById(1)).thenReturn(user);

        userService.removeAdminStatus(1);

        User updatedUser = userService.getUserById(1);
        assertNotNull(updatedUser);
        assertEquals(UserRole.ROLE_USER, updatedUser.getRole());
    }

    @Test
    public void testRemoveAdminStatusToNotAdmin() {
        User user = User.builder()
                .id(1)
                .email("min0@list.ru")
                .username("us")
                .role(UserRole.ROLE_USER)
                .gender(UserGender.MALE)
                .build();

        when(userRepository.findById(1)).thenReturn(user);

        userService.removeAdminStatus(1);

        User updatedUser = userService.getUserById(1);
        assertNotNull(updatedUser);
        assertEquals(UserRole.ROLE_USER, updatedUser.getRole());
    }
}