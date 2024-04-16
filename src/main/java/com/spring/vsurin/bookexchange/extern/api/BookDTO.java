package com.spring.vsurin.bookexchange.extern.api;

import com.spring.vsurin.bookexchange.domain.BookGenre;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.time.Year;
import java.util.List;

@Data
public class BookDTO extends RepresentationModel<BookDTO> {

    private long id;

    private List<Long> ownersIds;

    @NotNull
    @NotEmpty
    @Size(max = 300)
    private String title;

    @NotNull
    @NotEmpty
    @Size(max = 300)
    private String author;

    @NotNull
    private Year publicationYear;

    @NotNull
    @NotEmpty
    @Size(max = 20)
    private String isbn;

    @NotNull
    private BookGenre genre;

    @Size(max = 1000)
    private String description;

    private List<Long> userIdsOfferingForExchange;

    private Iterable<Integer> marks;
}
