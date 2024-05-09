package com.spring.vsurin.bookexchange;

import com.spring.vsurin.bookexchange.app.BookService;
import com.spring.vsurin.bookexchange.app.EmailService;
import com.spring.vsurin.bookexchange.app.RequestService;
import com.spring.vsurin.bookexchange.app.UserService;
import com.spring.vsurin.bookexchange.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RequestServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private RequestService requestService;

    @MockBean
    private EmailService emailService;

    @Sql("/with_offered2.sql")
    @Test
    public void testCreateRequest() {
    Request request = new Request(1, userService.getUserById(1), userService.getUserById(2), bookService.getBookById(3), bookService.getBookById(1), RequestStatus.ACTUAL, "");
        Request savedReq1 = requestService.createRequest(request);
        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());

        verify(emailService, times(1)).sendEmail(anyString(), anyString(), anyString());
        assertNotNull(savedReq1.getId());
    }

    @Sql("/with_req.sql")
    @Test
    public void testGetRequestById() {
        Request retrievedReq = requestService.getRequestById(1);
        assertNotNull(retrievedReq);
        assertEquals(1, retrievedReq.getId());
    }

    @Sql("/with_req.sql")
    @Test
    public void testGetRequestByIdNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            requestService.getRequestById(5);
        });
    }

    @Sql("/with_req.sql")
    @Test
    public void testAcceptRequest() {
        requestService.acceptRequest(1, 1);
        verify(emailService, times(2)).sendEmail(anyString(), anyString(), anyString());

        assertEquals(RequestStatus.ACCEPTED, requestService.getRequestById(1).getStatus());
        assertEquals(RequestStatus.REJECTED, requestService.getRequestById(2).getStatus());
    }

    @Sql("/with_req.sql")
    @Test
    public void testAcceptRejectedRequest() {
        requestService.rejectRequest(1);
        requestService.acceptRequest(1, 1);
        verify(emailService, times(1)).sendEmail(anyString(), anyString(), anyString());

        assertEquals(RequestStatus.REJECTED, requestService.getRequestById(1).getStatus());
    }

    @Sql("/with_req.sql")
    @Test
    public void testAcceptRequestNoRejectedRequests() {
        requestService.acceptRequest(1, 1);
        verify(emailService, times(2)).sendEmail(anyString(), anyString(), anyString());

        assertEquals(RequestStatus.ACCEPTED, requestService.getRequestById(1).getStatus());
        assertEquals(RequestStatus.ACTUAL, requestService.getRequestById(3).getStatus());
    }

    @Sql("/with_req.sql")
    @Test
    public void testRejectRequest() {
        requestService.rejectRequest(1);
        verify(emailService, times(1)).sendEmail(anyString(), anyString(), anyString());

        assertEquals(RequestStatus.REJECTED, requestService.getRequestById(1).getStatus());
        assertEquals(RequestStatus.ACTUAL, requestService.getRequestById(2).getStatus());
    }

    @Sql("/with_req.sql")
    @Test
    public void testFindByStatusAndSender() {
        requestService.rejectRequest(1);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Request> reqsPage = requestService.findByStatusAndSender(RequestStatus.ACTUAL, 3, pageable);
        List<Request> reqs = reqsPage.getContent();
        assertEquals(2, reqs.size());
    }

    @Sql("/with_req.sql")
    @Test
    public void testFindByStatusAndReceiver() {
        requestService.rejectRequest(1);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Request> reqsPage = requestService.findByStatusAndReceiver(RequestStatus.REJECTED, 2, pageable);
        List<Request> reqs = reqsPage.getContent();
        assertEquals(1, reqs.size());
    }

    @Sql("/with_req.sql")
    @Test
    public void testUpdateComment() {
        requestService.updateComment(1, "DEEEESC");

        Request updatedRequest = requestService.getRequestById(1);

        assertEquals("DEEEESC", updatedRequest.getCommentForReceiver());
    }
}
