package com.spring.vsurin.bookexchange.extern.api;

import com.spring.vsurin.bookexchange.domain.Book;
import com.spring.vsurin.bookexchange.domain.User;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BookAssembler extends RepresentationModelAssemblerSupport<Book, BookDTO> {

    public BookAssembler(UserAssembler userAssembler) {
        super(BookController.class, BookDTO.class);
    }

    @Override
    public BookDTO toModel(Book book) {
        BookDTO bookDTO = instantiateModel(book);

        bookDTO.setId(book.getId());
        bookDTO.setOwnersIds(book.getOwners().stream()
                .map(User::getId)
                .collect(Collectors.toList()));
        bookDTO.setUserIdsOfferingForExchange(book.getUsersOfferingForExchange().stream()
                .map(User::getId)
                .collect(Collectors.toList()));
        bookDTO.setTitle(book.getTitle());
        bookDTO.setAuthor(book.getAuthor());
        bookDTO.setPublicationYear(book.getPublicationYear());
        bookDTO.setIsbn(book.getIsbn());
        bookDTO.setGenre(book.getGenre());
        bookDTO.setDescription(book.getDescription());
        bookDTO.setMarks(book.getMarks());
        bookDTO.setRating(book.getRating());
        bookDTO.setUserIdsHaveInWishlist(book.getUsersHaveInWishlist().stream()
                .map(User::getId)
                .collect(Collectors.toList()));

        bookDTO.add(linkTo(methodOn(BookController.class).getBookCover(book.getId())).withRel("book_cover"));
        bookDTO.add(linkTo(methodOn(BookController.class).getBookById(book.getId())).withSelfRel());

        return bookDTO;
    }
}
