package com.spring.vsurin.bookexchange.extern.api;

import com.spring.vsurin.bookexchange.domain.Book;
import com.spring.vsurin.bookexchange.domain.Exchange;
import com.spring.vsurin.bookexchange.domain.User;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ExchangeAssembler extends RepresentationModelAssemblerSupport<Exchange, ExchangeDTO> {

    public ExchangeAssembler() {
        super(ExchangeController.class, ExchangeDTO.class);
    }

    @Override
    public ExchangeDTO toModel(Exchange exchange) {
        ExchangeDTO exchangeDTO = instantiateModel(exchange);

        exchangeDTO.setId(exchange.getId());
        exchangeDTO.setMember1Id(exchange.getMember1().getId());
        exchangeDTO.setMember2Id(exchange.getMember2().getId());
        exchangeDTO.setExchangedBook1Id(exchange.getExchangedBook1().getId());
        exchangeDTO.setExchangedBook2Id(exchange.getExchangedBook2().getId());
        exchangeDTO.setAddress1(exchange.getAddress1());
        exchangeDTO.setAddress2(exchange.getAddress2());
        exchangeDTO.setTrack1(exchange.getTrack1());
        exchangeDTO.setTrack2(exchange.getTrack2());
        exchangeDTO.setReceived1(exchange.isReceived1());
        exchangeDTO.setReceived2(exchange.isReceived2());
        exchangeDTO.setStatus(exchange.getStatus());
        exchangeDTO.setDate(exchange.getDate());

        // exchangeDTO.add(linkTo(methodOn(ExchangeController.class).getExchangeById(exchange.getId())).withSelfRel());

        return exchangeDTO;
    }
}
