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

@Tag(name = "���������� �������", description = "API ��� ���������� �������")
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

    @Operation(summary = "������� �����", description = "������� ����� ����� � ���� ������")
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

    @Operation(summary = "�������� ����� �� ID", description = "�������� ���������� � ����� �� �� ID")
    @GetMapping("/{bookId}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable long bookId) {
        Book book = bookService.getBookById(bookId);
        return ResponseEntity.ok(bookAssembler.toModel(book));
    }

    @Operation(summary = "������� �����", description = "������� ����� �� ���� ������ �� �� ID")
    @DeleteMapping("/delete/{bookId}")
    public ResponseEntity<Void> deleteBook(@PathVariable long bookId) {
        bookService.deleteBook(bookId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "�������� ��� �����", description = "�������� ������ ���� ���� � ����������")
    @GetMapping
    public ResponseEntity<Page<BookDTO>> getAllBooks(Pageable pageable) {
        Page<BookDTO> booksPage = bookService.getAllBooksInBase(pageable).map(bookAssembler::toModel);
        return ResponseEntity.ok(booksPage);
    }

    @Operation(summary = "����� ���� �� �����", description = "�������� ������ ���� ������������� ����� � ����������")
    @GetMapping("/genre/{genre}")
    public ResponseEntity<Page<BookDTO>> searchByGenre(@PathVariable BookGenre genre, Pageable pageable) {
        Page<BookDTO> booksPage = bookService.searchByGenre(genre, pageable).map(bookAssembler::toModel);
        return ResponseEntity.ok(booksPage);
    }

    @Operation(summary = "����� ���� �� �������� ��� ������", description = "�������� ������ ���� �� ���������� ������� (�������� ��� ������) � ����������")
    @GetMapping("/search")
    public ResponseEntity<Page<BookDTO>> searchByTitleOrAuthor(@RequestParam String searchTerm, Pageable pageable) {
        Page<BookDTO> booksPage = bookService.searchByTitleOrAuthor(searchTerm, pageable).map(bookAssembler::toModel);
        return ResponseEntity.ok(booksPage);
    }

    @Operation(summary = "�������� ����� ��������� ��� ������", description = "�������� ������ ����, ��������� ��� ������ � ����������")
    @GetMapping("/available-for-exchange")
    public ResponseEntity<Page<BookDTO>> getAvailableForExchangeBooks(Pageable pageable) {
        Page<BookDTO> booksPage = bookService.getAvailableForExchangeBooks(pageable).map(bookAssembler::toModel);
        return ResponseEntity.ok(booksPage);
    }

    @Operation(summary = "��������� ������ � �����", description = "��������� ����� ������ � �����")
    @PutMapping("/{bookId}/marks")
    public ResponseEntity<Void> addMarkToBook(@PathVariable long bookId, @RequestBody @Valid BookUpdateDTO updateDTO) {
        bookService.addMarkToBook(bookId, updateDTO.getAddedMark());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "��������� �������� � �����", description = "��������� �������� � �����")
    @PutMapping("/{bookId}/description")
    public ResponseEntity<Void> updateDescriptionToBook(@PathVariable long bookId, @RequestBody @Valid BookUpdateDTO updateDTO) {
        bookService.updateDescriptionToBook(bookId, updateDTO.getDescription());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "�������� ������� �����", description = "�������� ������� ����� �� �� ID")
    @GetMapping("/{bookId}/cover")
    public ResponseEntity<byte[]> getBookCover(@PathVariable long bookId) {
        byte[] coverImage = bookService.getBookCover(bookId);
        return ResponseEntity.ok(coverImage);
    }

    @Operation(summary = "��������� ������� �����", description = "��������� ������� ����� �� �� ID")
    @PutMapping("/{bookId}/cover")
    public ResponseEntity<Void> updateCoverToBook(@PathVariable long bookId, @RequestBody byte[] image) {
        bookService.updateBookCover(bookId, image);
        return ResponseEntity.ok().build();
    }
}
