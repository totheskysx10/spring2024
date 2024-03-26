package com.spring.vsurin.bookexchange.app;

import com.spring.vsurin.bookexchange.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    Request findById(long id);

    List<Request> findByStatusAndBookSenderWants(RequestStatus status, Book book);

    Page<Request> findByStatusAndSenderId(RequestStatus status, long senderId, Pageable pageable);
    Page<Request> findByStatusAndReceiverId(RequestStatus status, long receiverId, Pageable pageable);
}
