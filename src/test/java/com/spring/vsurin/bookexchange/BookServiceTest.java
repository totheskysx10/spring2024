package com.spring.vsurin.bookexchange;

import com.spring.vsurin.bookexchange.app.*;
import com.spring.vsurin.bookexchange.domain.Book;
import com.spring.vsurin.bookexchange.domain.BookGenre;
import com.spring.vsurin.bookexchange.domain.User;
import com.spring.vsurin.bookexchange.domain.UserGender;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJpaTest
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookService bookService;


    @Test
    public void testCreateBook() {
        Book book = Book.builder()
                .title("Test Book 4")
                .author("Test Author 4")
                .description("Test description")
                .genre(BookGenre.ART)
                .isbn("101")
                .publicationYear(Year.of(2010))
                .build();

        when(bookRepository.save(book)).thenReturn(book);

        Book savedBook = bookService.createBook(book);
        assertNotNull(savedBook.getId());
    }

    @Test
    public void testGetBookById() {
        Book testBook = Book.builder()
                .id(1)
                .title("Test Book 4")
                .author("Test Author 4")
                .description("Test description")
                .genre(BookGenre.ART)
                .isbn("101")
                .publicationYear(Year.of(2010))
                .build();

        when(bookRepository.findById(1)).thenReturn(testBook);

        Book retrievedBook = bookService.getBookById(1);
        assertNotNull(retrievedBook);
        assertEquals(1, retrievedBook.getId());
    }

    @Test
    public void testGetBookByIdNull() {
        when(bookRepository.findById(4)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> {
            bookService.getBookById(4);
        });
    }

    @Test
    public void testGetAllBooksInBase() {
        List<Book> books = new ArrayList<>();
        books.add(new Book());
        books.add(new Book());
        books.add(new Book());

        Page<Book> booksPage = mock(Page.class);
        when(booksPage.getContent()).thenReturn(books);
        when(bookRepository.findAll(any(Pageable.class))).thenReturn(booksPage);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> resultPage = bookService.getAllBooksInBase(pageable);
        assertEquals(3, resultPage.getContent().size());
    }

    @Test
    public void testSearchByGenre() {
        List<Book> books = new ArrayList<>();
        books.add(new Book());

        Page<Book> booksPage = mock(Page.class);
        when(booksPage.getContent()).thenReturn(books);
        when(bookRepository.findByGenre(eq(BookGenre.FICTION), any(Pageable.class))).thenReturn(booksPage);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> resultPage = bookService.searchByGenre(BookGenre.FICTION, pageable);
        assertEquals(1, resultPage.getContent().size());
    }

    @Test
    public void testSearchByTitleOrAuthor() {
        List<Book> books = new ArrayList<>();
        books.add(new Book());
        books.add(new Book());
        books.add(new Book());

        Page<Book> booksPage = mock(Page.class);
        when(booksPage.getContent()).thenReturn(books);
        when(bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(eq("Test Bo"), eq("Test Bo"), any(Pageable.class))).thenReturn(booksPage);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> resultPage = bookService.searchByTitleOrAuthor("Test Bo", pageable);
        assertEquals(3, resultPage.getContent().size());
    }

    @Test
    public void testGetAvailableForExchangeBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book());

        Page<Book> booksPage = mock(Page.class);
        when(booksPage.getContent()).thenReturn(books);
        when(bookRepository.findBooksWithUsersOfferingForExchange(any(Pageable.class))).thenReturn(booksPage);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> resultPage = bookService.getAvailableForExchangeBooks(pageable);
        assertEquals(1, resultPage.getContent().size());
    }

    @Test
    public void testAddMarkToBook() {
        Book testBook = Book.builder()
                .id(1)
                .title("Test Book 4")
                .author("Test Author 4")
                .description("Test description")
                .genre(BookGenre.ART)
                .isbn("101")
                .publicationYear(Year.of(2010))
                .marks(new ArrayList<>())
                .owners(new ArrayList<>())
                .build();

        User testUser = User.builder()
                .id(1)
                .email("test@test.ru")
                .addressList(new ArrayList<>())
                .library(new ArrayList<>())
                .gender(UserGender.MALE)
                .build();
        testUser.getLibrary().add(testBook);

        when(bookRepository.findById(1)).thenReturn(testBook);
        when(userRepository.findById(1)).thenReturn(testUser);

        bookService.addMarkToBook(1, 5, 1);
        bookService.addMarkToBook(1, 7, 1);

        verify(bookRepository, times(2)).save(any(Book.class));

        Book updatedBook = bookService.getBookById(1);
        Iterable<Integer> marks = updatedBook.getMarks();
        int count = 0;
        for (Integer mark : marks) {
            count++;
        }

        assertEquals(2, count);
    }

    @Test
    public void testAddMarkToBookNotInLibrary() {
        Book testBook = Book.builder()
                .id(1)
                .title("Test Book 4")
                .author("Test Author 4")
                .description("Test description")
                .genre(BookGenre.ART)
                .isbn("101")
                .publicationYear(Year.of(2010))
                .marks(new ArrayList<>())
                .owners(new ArrayList<>())
                .build();

        User testUser = User.builder()
                .id(1)
                .email("test@test.ru")
                .addressList(new ArrayList<>())
                .library(new ArrayList<>())
                .gender(UserGender.MALE)
                .build();

        when(bookRepository.findById(1)).thenReturn(testBook);
        when(userRepository.findById(1)).thenReturn(testUser);

        bookService.addMarkToBook(1, 5, 1);
        bookService.addMarkToBook(1, 7, 1);

        verify(bookRepository, times(0)).save(any(Book.class));

        Book updatedBook = bookService.getBookById(1);
        Iterable<Integer> marks = updatedBook.getMarks();
        int count = 0;
        for (Integer mark : marks) {
            count++;
        }

        assertEquals(0, count);
    }

    @Test
    public void testUpdateDescriptionToBook() {
        Book testBook = Book.builder()
                .id(1)
                .title("Test Book 4")
                .author("Test Author 4")
                .description("Test description")
                .genre(BookGenre.ART)
                .isbn("101")
                .publicationYear(Year.of(2010))
                .build();

        when(bookRepository.findById(1)).thenReturn(testBook);

        bookService.updateDescriptionToBook(1, "DEEEESC");

        Book updatedBook = bookService.getBookById(1);
        assertEquals("DEEEESC", updatedBook.getDescription());
    }

    @Test
    public void testGetBookCover() {
        byte[] image = new byte[]{1, 2, 3};

        Book testBook = Book.builder()
                .id(1)
                .title("Test Book 4")
                .author("Test Author 4")
                .description("Test description")
                .genre(BookGenre.ART)
                .isbn("101")
                .publicationYear(Year.of(2010))
                .coverImage(image)
                .build();

        BookCoverProjection bookCoverProjection = () -> image;

        when(bookRepository.findById(1)).thenReturn(testBook);
        when(bookRepository.findCoverImageById(1)).thenReturn(bookCoverProjection);

        byte[] real = bookService.getBookCover(1);
        assertEquals(image, real);
    }


    @Test
    public void testUpdateBookCover() {
        Book testBook = Book.builder()
                .id(1)
                .title("Test Book 4")
                .author("Test Author 4")
                .description("Test description")
                .genre(BookGenre.ART)
                .isbn("101")
                .publicationYear(Year.of(2010))
                .build();

        when(bookRepository.findById(1)).thenReturn(testBook);

        byte[] newImage = new byte[]{4, 5, 6};
        bookService.updateBookCover(1, newImage);
        Book updatedBook = bookService.getBookById(1);

        assertEquals(newImage, updatedBook.getCoverImage());
    }
}
