package com.spring.vsurin.bookexchange.extern.api;

import com.spring.vsurin.bookexchange.app.BookService;
import com.spring.vsurin.bookexchange.domain.Book;
import com.spring.vsurin.bookexchange.domain.BookGenre;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "Управление книгами", description = "API для управления книгами")
@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    private final BookAssembler bookAssembler;

    @Autowired
    public BookController(BookService bookService, BookAssembler bookAssembler) {
        this.bookService = bookService;
        this.bookAssembler = bookAssembler;
    }

    @Operation(summary = "Создает книгу", description = "Создает новую книгу в базе данных")
    @PostMapping
    public ResponseEntity<BookDTO> createBook(@RequestBody @Valid BookDTO bookDTO) {
        Book newBook = Book.builder()
                .title(bookDTO.getTitle())
                .author(bookDTO.getAuthor())
                .publicationYear(bookDTO.getPublicationYear())
                .isbn(bookDTO.getIsbn())
                .genre(bookDTO.getGenre())
                .description(bookDTO.getDescription())
                .owners(new ArrayList<>())
                .usersOfferingForExchange(new ArrayList<>())
                .marks((List<Integer>) bookDTO.getMarks())
                .usersHaveInWishlist(new ArrayList<>())
                .rating(bookDTO.getRating())
                .build();

        bookService.createBook(newBook);

        return new ResponseEntity<>(bookAssembler.toModel(newBook), HttpStatus.CREATED);
    }

    @Operation(summary = "Получает книгу по ID", description = "Получает информацию о книге по ее ID")
    @GetMapping("/{bookId}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable long bookId) {
        Book book = bookService.getBookById(bookId);
        return ResponseEntity.ok(bookAssembler.toModel(book));
    }

    @Operation(summary = "Удаляет книгу", description = "Удаляет книгу из базы данных по ее ID")
    @DeleteMapping("/delete/{bookId}")
    public ResponseEntity<Void> deleteBook(@PathVariable long bookId) {
        bookService.deleteBook(bookId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получает все книги", description = "Получает список всех книг с пагинацией")
    @GetMapping
    public ResponseEntity<Page<BookDTO>> getAllBooks(Pageable pageable) {
        Page<BookDTO> booksPage = bookService.getAllBooksInBase(pageable).map(bookAssembler::toModel);
        return ResponseEntity.ok(booksPage);
    }

    @Operation(summary = "Поиск книг по жанру", description = "Получает список книг определенного жанра с пагинацией")
    @GetMapping("/genre/{genre}")
    public ResponseEntity<Page<BookDTO>> searchByGenre(@PathVariable BookGenre genre, Pageable pageable) {
        Page<BookDTO> booksPage = bookService.searchByGenre(genre, pageable).map(bookAssembler::toModel);
        return ResponseEntity.ok(booksPage);
    }

    @Operation(summary = "Поиск книг по названию или автору", description = "Получает список книг по поисковому запросу (названию или автору) с пагинацией")
    @GetMapping("/search")
    public ResponseEntity<Page<BookDTO>> searchByTitleOrAuthor(@RequestParam String searchTerm, Pageable pageable) {
        Page<BookDTO> booksPage = bookService.searchByTitleOrAuthor(searchTerm, pageable).map(bookAssembler::toModel);
        return ResponseEntity.ok(booksPage);
    }

    @Operation(summary = "Получает книги доступные для обмена", description = "Получает список книг, доступных для обмена с пагинацией")
    @GetMapping("/available-for-exchange")
    public ResponseEntity<Page<BookDTO>> getAvailableForExchangeBooks(Pageable pageable) {
        Page<BookDTO> booksPage = bookService.getAvailableForExchangeBooks(pageable).map(bookAssembler::toModel);
        return ResponseEntity.ok(booksPage);
    }

    @Operation(summary = "Добавляет оценку к книге", description = "Добавляет новую оценку к книге")
    @PutMapping("/{bookId}/marks")
    public ResponseEntity<Void> addMarkToBook(@PathVariable long bookId, @RequestBody @Valid BookUpdateDTO updateDTO) {
        bookService.addMarkToBook(bookId, updateDTO.getAddedMark());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Обновляет описание к книге", description = "Обновляет описание к книге")
    @PutMapping("/{bookId}/description")
    public ResponseEntity<Void> updateDescriptionToBook(@PathVariable long bookId, @RequestBody @Valid BookUpdateDTO updateDTO) {
        bookService.updateDescriptionToBook(bookId, updateDTO.getDescription());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Получает обложку книги", description = "Получает обложку книги по ее ID")
    @GetMapping("/{bookId}/cover")
    public ResponseEntity<byte[]> getBookCover(@PathVariable long bookId) {
        byte[] coverImage = bookService.getBookCover(bookId);
        return ResponseEntity.ok(coverImage);
    }

    @Operation(summary = "Обновляет обложку книги", description = "Обновляет обложку книги по ее ID")
    @PutMapping("/{bookId}/cover")
    public ResponseEntity<Void> updateCoverToBook(@PathVariable long bookId, @RequestBody byte[] image) {
        bookService.updateBookCover(bookId, image);
        return ResponseEntity.ok().build();
    }
}
