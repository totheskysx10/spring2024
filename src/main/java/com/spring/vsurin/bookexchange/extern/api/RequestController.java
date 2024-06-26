package com.spring.vsurin.bookexchange.extern.api;

import com.spring.vsurin.bookexchange.app.BookService;
import com.spring.vsurin.bookexchange.app.RequestService;
import com.spring.vsurin.bookexchange.app.UserService;
import com.spring.vsurin.bookexchange.domain.Request;
import com.spring.vsurin.bookexchange.domain.RequestStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "���������� ��������", description = "API ��� ���������� �������� �� ����� �������")
@RestController
@RequestMapping("/requests")
public class RequestController {

    private final RequestService requestService;
    private final RequestAssembler requestAssembler;
    private final UserService userService;
    private final BookService bookService;

    @Autowired
    public RequestController(RequestService requestService, RequestAssembler requestAssembler, UserService userService, BookService bookService) {
        this.requestService = requestService;
        this.requestAssembler = requestAssembler;
        this.userService = userService;
        this.bookService = bookService;
    }

    @Operation(summary = "������� ����� ������", description = "������� ����� ������ �� ����� �������")
    @PostMapping
    public ResponseEntity<RequestDTO> createRequest(@RequestBody @Valid RequestDTO requestDTO) {
        Request newRequest = Request.builder()
                .sender(userService.getUserById(requestDTO.getSenderId()))
                .receiver(userService.getUserById(requestDTO.getReceiverId()))
                .bookSenderWants(bookService.getBookById(requestDTO.getBookSenderWantsId()))
                .commentForReceiver(requestDTO.getCommentForReceiver())
                .build();

        requestService.createRequest(newRequest);

        return new ResponseEntity<>(requestAssembler.toModel(newRequest), HttpStatus.CREATED);
    }

    @Operation(summary = "���������� ������ �� ID", description = "���������� ������ �� ����� ������� �� �� ID")
    @GetMapping("/{requestId}")
    public ResponseEntity<RequestDTO> getRequestById(@PathVariable long requestId) {
        Request request = requestService.getRequestById(requestId);
        return ResponseEntity.ok(requestAssembler.toModel(request));
    }

    @Operation(summary = "��������� ������", description = "��������� ������ �� ����� �������")
    @PutMapping("/{requestId}/accept")
    public ResponseEntity<Void> acceptRequest(@PathVariable long requestId, @RequestParam long bookId) {
        requestService.acceptRequest(requestId, bookId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "��������� ������", description = "��������� ������ �� ����� �������")
    @PutMapping("/{requestId}/reject")
    public ResponseEntity<Void> rejectRequest(@PathVariable long requestId) {
        requestService.rejectRequest(requestId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "���������� ������ ����������� �� ������� � �����������", description = "���������� ������ ����������� �� ���������� ������� � �����������")
    @GetMapping("/requests/sender")
    public ResponseEntity<Page<Request>> findByStatusAndSender(
            @RequestParam RequestStatus status,
            @RequestParam long senderId,
            Pageable pageable) {
        Page<Request> requestsPage = requestService.findByStatusAndSender(status, senderId, pageable);
        return ResponseEntity.ok(requestsPage);
    }

    @Operation(summary = "���������� ������ ����������� �� ������� � ����������", description = "���������� ������ ����������� �� ���������� ������� � ����������")
    @GetMapping("/requests/receiver")
    public ResponseEntity<Page<Request>> findByStatusAndReceiver(
            @RequestParam RequestStatus status,
            @RequestParam long receiverId,
            Pageable pageable) {
        Page<Request> requestsPage = requestService.findByStatusAndReceiver(status, receiverId, pageable);
        return ResponseEntity.ok(requestsPage);
    }

    @Operation(summary = "��������� ����������� � ������", description = "��������� ����������� � ������")
    @PutMapping("/{requestId}/comment")
    public ResponseEntity<Void> updateComment(@PathVariable long requestId, @RequestParam String comment) {
        requestService.updateComment(requestId, comment);
        return ResponseEntity.ok().build();
    }
}
