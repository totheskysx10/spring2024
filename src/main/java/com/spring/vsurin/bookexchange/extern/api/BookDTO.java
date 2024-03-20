package com.spring.vsurin.bookexchange.extern.api;

import com.spring.vsurin.bookexchange.domain.BookGenre;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.time.Year;
import java.util.List;

@Data
public class BookDTO extends RepresentationModel<BookDTO> {
    private long id;
    private List<Long> ownersIds;
    private String title;
    private String author;
    private Year publicationYear;
    private String isbn;
    private BookGenre genre;
    private String description;
    private List<Long> userIdsOfferingForExchange;
    private List<Integer> marks;
}
