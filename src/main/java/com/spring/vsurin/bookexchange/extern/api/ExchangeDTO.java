package com.spring.vsurin.bookexchange.extern.api;

import com.spring.vsurin.bookexchange.domain.ExchangeStatus;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

@Data
public class ExchangeDTO extends RepresentationModel<ExchangeDTO> {
    private long id;
    private long member1Id;
    private long member2Id;
    private long exchangedBook1Id;
    private long exchangedBook2Id;
    private String address1;
    private String address2;
    private String track1;
    private String track2;
    private boolean received1;
    private boolean received2;
    private ExchangeStatus status;
    private LocalDate date;
}
