package com.spring.vsurin.bookexchange.extern.api;

import com.spring.vsurin.bookexchange.domain.RequestStatus;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class RequestDTO extends RepresentationModel<RequestDTO> {
    private long id;
    private long senderId;
    private long receiverId;
    private long bookSenderWantsId;
    private Long bookReceiverWantsId;
    private RequestStatus status;
    private String commentForReceiver;
}
