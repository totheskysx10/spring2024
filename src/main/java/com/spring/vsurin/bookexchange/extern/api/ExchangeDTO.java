package com.spring.vsurin.bookexchange.extern.api;

import com.spring.vsurin.bookexchange.domain.ExchangeStatus;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

@Data
public class ExchangeDTO extends RepresentationModel<ExchangeDTO> {

    private long id;

    @Positive
    private long member1Id;

    @Positive
    private long member2Id;

    @Positive
    private long exchangedBook1Id;

    @Positive
    private long exchangedBook2Id;

    @NotNull
    @NotEmpty
    @Size(min = 50)
    private String address1;

    @NotNull
    @NotEmpty
    @Size(min = 50)
    private String address2;

    @NotNull
    @NotEmpty
    @Size(min = 5, max = 15)
    private String track1;

    @NotNull
    @NotEmpty
    @Size(min = 5, max = 15)
    private String track2;

    private boolean received1;

    private boolean received2;

    @NotNull
    private ExchangeStatus status;

    @PastOrPresent
    private LocalDate date;
}
