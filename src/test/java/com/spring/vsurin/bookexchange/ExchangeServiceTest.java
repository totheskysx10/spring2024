package com.spring.vsurin.bookexchange;

import com.spring.vsurin.bookexchange.app.EmailService;
import com.spring.vsurin.bookexchange.app.ExchangeRepository;
import com.spring.vsurin.bookexchange.app.ExchangeService;
import com.spring.vsurin.bookexchange.app.UserService;
import com.spring.vsurin.bookexchange.domain.EmailData;
import com.spring.vsurin.bookexchange.domain.Exchange;
import com.spring.vsurin.bookexchange.domain.ExchangeStatus;
import com.spring.vsurin.bookexchange.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class ExchangeServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private ExchangeService exchangeService;

    @Autowired
    private ExchangeRepository exchangeRepository;

    @MockBean
    private EmailService emailService;

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
        verify(emailService, times(4)).sendEmail(any(EmailData.class));

        assertEquals("123", updEx.getTrack1());
        assertEquals("1234", updEx.getTrack2());
    }

    @Sql("/with_exchanges.sql")
    @Test
    public void testSetNoTrack() {
        exchangeService.setNoTrack(1, 1);
        exchangeService.setNoTrack(2, 1);
        Exchange updEx = exchangeService.getExchangeById(1);
        verify(emailService, times(4)).sendEmail(any(EmailData.class));

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
        User user = userService.getUserById(1);
        assertEquals(1, user.getWishlist().size());

        exchangeService.setNoTrack(1, 1);
        exchangeService.setNoTrack(2, 1);

        exchangeService.receiveBook(1, 1);
        exchangeService.receiveBook(1, 2);

        User updatedUser = userService.getUserById(1);
        User updatedUser2 = userService.getUserById(2);

        verify(emailService, times(8)).sendEmail(any(EmailData.class));

        assertEquals(ExchangeStatus.COMPLETED, exchangeService.getExchangeById(1).getStatus());
        assertEquals(2, updatedUser.getLibrary().get(0).getId());
        assertEquals(1, updatedUser2.getLibrary().get(1).getId());
        assertEquals(0, updatedUser.getWishlist().size());
    }

    @Sql("/with_exchanges.sql")
    @Test
    public void testSetProblemsStatus() {
        Exchange ex = exchangeService.getExchangeById(1);
        ex.setCurrentDate();
        exchangeRepository.save(ex);
        exchangeService.setNoTrack(1, 1);
        exchangeService.setNoTrack(2, 1);
        verify(emailService, times(4)).sendEmail(any(EmailData.class));

        assertThrows(IllegalStateException.class, () -> {
            exchangeService.setProblemsStatus(1);
        });
    }

    @Sql("/with_exchanges.sql")
    @Test
    public void testSetProblemsStatusCorrectDate() {
        Exchange ex = exchangeService.getExchangeById(1);
        ex.setDate(LocalDate.now().minusDays(45));
        exchangeRepository.save(ex);
        exchangeService.setNoTrack(1, 1);
        exchangeService.setNoTrack(2, 1);
        exchangeService.setProblemsStatus(ex.getId());
        verify(emailService, times(6)).sendEmail(any(EmailData.class));

        assertEquals(ExchangeStatus.PROBLEMS, exchangeService.getExchangeById(ex.getId()).getStatus());
    }

    @Sql("/with_exchanges.sql")
    @Test
    public void testCancelExchange() {
        Exchange ex = exchangeService.getExchangeById(1);

        exchangeService.setNoTrack(1, 1);
        exchangeService.setNoTrack(2, 1);
        exchangeService.cancelExchange(ex.getId());
        verify(emailService, times(6)).sendEmail(any(EmailData.class));

        assertEquals(ExchangeStatus.CANCELLED_BY_ADMIN, exchangeService.getExchangeById(ex.getId()).getStatus());
    }
}
