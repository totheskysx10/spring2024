package com.spring.vsurin.bookexchange;

import com.spring.vsurin.bookexchange.app.BookService;
import com.spring.vsurin.bookexchange.app.RequestService;
import com.spring.vsurin.bookexchange.app.UserService;
import com.spring.vsurin.bookexchange.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RequestServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private RequestService requestService;

    @Sql("/with_offered2.sql")
    @Test
    public void testCreateRequest() {
        Request request = new Request();
        request.setSender(userService.getUserById(1));
        request.setReceiver(userService.getUserById(2));
        request.setBookSenderWants(bookService.getBookById(3));
        Request savedReq1 = requestService.createRequest(request);
        assertNotNull(savedReq1.getId());
    }

    @Sql("/with_offered2.sql")
    @Test
    public void testGetRequestById() {
        Request request = new Request();
        request.setSender(userService.getUserById(1));
        request.setReceiver(userService.getUserById(2));
        request.setBookSenderWants(bookService.getBookById(3));
        Request savedReq1 = requestService.createRequest(request);

        long requestId = savedReq1.getId();
        Request retrievedReq = requestService.getRequestById(requestId);
        assertNotNull(retrievedReq);
        assertEquals(requestId, retrievedReq.getId());
    }

    @Sql("/with_offered2.sql")
    @Test
    public void testGetRequestByIdNull() {
        Request request = new Request();
        request.setSender(userService.getUserById(1));
        request.setReceiver(userService.getUserById(2));
        request.setBookSenderWants(bookService.getBookById(3));
        Request savedReq1 = requestService.createRequest(request);

        assertThrows(IllegalArgumentException.class, () -> {
            requestService.getRequestById(2);
        });
    }

    @Sql("/with_offered2.sql")
    @Test
    public void testAcceptRequest() {
        Request request1 = new Request();
        request1.setSender(userService.getUserById(1));
        request1.setReceiver(userService.getUserById(2));
        request1.setBookSenderWants(bookService.getBookById(3));
        Request savedReq1 = requestService.createRequest(request1);

        Request request2 = new Request();
        request2.setSender(userService.getUserById(3));
        request2.setReceiver(userService.getUserById(2));
        request2.setBookSenderWants(bookService.getBookById(3));
        Request savedReq2 = requestService.createRequest(request2);

        requestService.acceptRequest(request1.getId(), 1);

        assertEquals(RequestStatus.ACCEPTED, requestService.getRequestById(1).getStatus());
        assertEquals(RequestStatus.REJECTED, requestService.getRequestById(2).getStatus());
    }

    @Sql("/with_offered2.sql")
    @Test
    public void testAcceptRequestNoRejectedRequests() {
        Request request1 = new Request();
        request1.setSender(userService.getUserById(1));
        request1.setReceiver(userService.getUserById(2));
        request1.setBookSenderWants(bookService.getBookById(3));
        Request savedReq1 = requestService.createRequest(request1);

        Request request2 = new Request();
        request2.setSender(userService.getUserById(3));
        request2.setReceiver(userService.getUserById(2));
        request2.setBookSenderWants(bookService.getBookById(2));
        Request savedReq2 = requestService.createRequest(request2);

        requestService.acceptRequest(request1.getId(), 1);

        assertEquals(RequestStatus.ACCEPTED, requestService.getRequestById(1).getStatus());
        assertEquals(RequestStatus.ACTUAL, requestService.getRequestById(2).getStatus());
    }

    @Sql("/with_offered2.sql")
    @Test
    public void testRejectRequest() {
        Request request1 = new Request();
        request1.setSender(userService.getUserById(1));
        request1.setReceiver(userService.getUserById(2));
        request1.setBookSenderWants(bookService.getBookById(3));
        Request savedReq1 = requestService.createRequest(request1);

        Request request2 = new Request();
        request2.setSender(userService.getUserById(3));
        request2.setReceiver(userService.getUserById(2));
        request2.setBookSenderWants(bookService.getBookById(3));
        Request savedReq2 = requestService.createRequest(request2);

        requestService.rejectRequest(request1.getId());

        assertEquals(RequestStatus.REJECTED, requestService.getRequestById(1).getStatus());
        assertEquals(RequestStatus.ACTUAL, requestService.getRequestById(2).getStatus());
    }

    @Sql("/with_offered2.sql")
    @Test
    public void testFindByStatusAndSender() {
        Request request1 = new Request();
        request1.setSender(userService.getUserById(1));
        request1.setReceiver(userService.getUserById(2));
        request1.setBookSenderWants(bookService.getBookById(3));
        Request savedReq1 = requestService.createRequest(request1);

        Request request2 = new Request();
        request2.setSender(userService.getUserById(3));
        request2.setReceiver(userService.getUserById(2));
        request2.setBookSenderWants(bookService.getBookById(3));
        Request savedReq2 = requestService.createRequest(request2);

        requestService.rejectRequest(request1.getId());

        Pageable pageable = PageRequest.of(0, 10);
        Page<Request> reqsPage = requestService.findByStatusAndSender(RequestStatus.ACTUAL, 3, pageable);
        List<Request> reqs = reqsPage.getContent();
        assertEquals(1, reqs.size());
    }

    @Sql("/with_offered2.sql")
    @Test
    public void testFindByStatusAndReceiver() {
        Request request1 = new Request();
        request1.setSender(userService.getUserById(1));
        request1.setReceiver(userService.getUserById(2));
        request1.setBookSenderWants(bookService.getBookById(3));
        Request savedReq1 = requestService.createRequest(request1);

        Request request2 = new Request();
        request2.setSender(userService.getUserById(3));
        request2.setReceiver(userService.getUserById(2));
        request2.setBookSenderWants(bookService.getBookById(3));
        Request savedReq2 = requestService.createRequest(request2);

        requestService.rejectRequest(request1.getId());

        Pageable pageable = PageRequest.of(0, 10);
        Page<Request> reqsPage = requestService.findByStatusAndReceiver(RequestStatus.REJECTED, 2, pageable);
        List<Request> reqs = reqsPage.getContent();
        assertEquals(1, reqs.size());
    }

    @Sql("/with_offered2.sql")
    @Test
    public void testUpdateComment() {
        Request request1 = new Request();
        request1.setSender(userService.getUserById(1));
        request1.setReceiver(userService.getUserById(2));
        request1.setBookSenderWants(bookService.getBookById(3));
        Request savedReq1 = requestService.createRequest(request1);

        requestService.updateComment(1, "DEEEESC");

        Request updatedRequest = requestService.getRequestById(1);

        assertEquals("DEEEESC", updatedRequest.getCommentForReceiver());
    }
}
