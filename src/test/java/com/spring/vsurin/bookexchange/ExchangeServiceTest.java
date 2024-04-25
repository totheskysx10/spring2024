package com.spring.vsurin.bookexchange;

import com.spring.vsurin.bookexchange.app.ExchangeRepository;
import com.spring.vsurin.bookexchange.app.ExchangeService;
import com.spring.vsurin.bookexchange.app.UserService;
import com.spring.vsurin.bookexchange.domain.Exchange;
import com.spring.vsurin.bookexchange.domain.ExchangeStatus;
import com.spring.vsurin.bookexchange.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ExchangeServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    private ExchangeRepository exchangeRepository;

    @Sql("/with_exchanges.sql")
    @Test
    public void testGetExchangeById() {
        long exchangeId = 1;
        Exchange retrievedEx = exchangeService.getExchangeById(exchangeId);
        assertNotNull(retrievedEx);
        assertEquals(exchangeId, retrievedEx.getId());
    }

    @Sql("/with_exchanges.sql")
    @Test
    public void testGetExchangeByIdNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            exchangeService.getExchangeById(4);
        });
    }

    @Sql("/with_exchanges.sql")
    @Test
    public void testUpdateTrackSetByUser() {
        exchangeService.updateTrackSetByUser(1, 1, "123");
        exchangeService.updateTrackSetByUser(2, 1, "1234");
        Exchange updEx = exchangeService.getExchangeById(1);

        assertEquals("123", updEx.getTrack1());
        assertEquals("1234", updEx.getTrack2());
    }

    @Sql("/with_exchanges.sql")
    @Test
    public void testSetNoTrack() {
        exchangeService.setNoTrack(1, 1);
        exchangeService.setNoTrack(2, 1);
        Exchange updEx = exchangeService.getExchangeById(1);

        assertEquals("DELIVERY_WITHOUT_TRACK", updEx.getTrack1());
        assertEquals("DELIVERY_WITHOUT_TRACK", updEx.getTrack2());
    }

    @Sql("/with_exchanges.sql")
    @Test
    public void testSearchByStatusAndMember() {
        exchangeService.setNoTrack(1, 1);
        exchangeService.setNoTrack(2, 1);

        exchangeService.setNoTrack(3, 2);
        exchangeService.setNoTrack(2, 2);

        List<Exchange> result = exchangeService.searchByStatusAndMember(ExchangeStatus.IN_PROGRESS, 1);

        assertEquals(1, result.size());
    }

    @Sql("/with_exchanges.sql")
    @Test
    public void testReceiveBooks() {
        exchangeService.setNoTrack(1, 1);
        exchangeService.setNoTrack(2, 1);

        exchangeService.receiveBook(1, 1);
        exchangeService.receiveBook(1, 2);

        User updatedUser = userService.getUserById(1);
        User updatedUser2 = userService.getUserById(2);

        assertEquals(ExchangeStatus.COMPLETED, exchangeService.getExchangeById(1).getStatus());
        assertEquals(2, updatedUser.getLibrary().get(0).getId());
        assertEquals(1, updatedUser2.getLibrary().get(1).getId());
    }

    @Sql("/with_exchanges.sql")
    @Test
    public void testSetProblemsStatus() {
        Exchange ex = exchangeService.getExchangeById(1);
        ex.setCurrentDate();
        exchangeRepository.save(ex);
        exchangeService.setNoTrack(1, 1);
        exchangeService.setNoTrack(2, 1);

        assertThrows(IllegalStateException.class, () -> {
            exchangeService.setProblemsStatus(1);
        });
    }
}
