package com.spring.vsurin.bookexchange;

import com.spring.vsurin.bookexchange.app.*;
import com.spring.vsurin.bookexchange.domain.Exchange;
import com.spring.vsurin.bookexchange.domain.RequestStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ExchangeTest {

    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    private RequestService requestService;

    @Sql("/with_req.sql")
    @Test
    public void testSetCurrentDate() {
        requestService.acceptRequest(1, 1);

        assertEquals(LocalDate.now(), exchangeService.getExchangeById(1).getDate());
    }
}
