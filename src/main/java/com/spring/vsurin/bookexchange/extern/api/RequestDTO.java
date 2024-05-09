package com.spring.vsurin.bookexchange.extern.api;

import com.spring.vsurin.bookexchange.domain.RequestStatus;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class RequestDTO extends RepresentationModel<RequestDTO> {

    private long id;

    @Positive
    private long senderId;

    @Positive
    private long receiverId;

    @Positive
    private long bookSenderWantsId;

    private long bookReceiverWantsId;

    private RequestStatus status;

    @Size(max = 300)
    private String commentForReceiver;
}
