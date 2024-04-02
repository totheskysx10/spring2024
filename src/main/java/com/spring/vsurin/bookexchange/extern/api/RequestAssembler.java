package com.spring.vsurin.bookexchange.extern.api;

import com.spring.vsurin.bookexchange.app.RequestService;
import com.spring.vsurin.bookexchange.domain.Request;
import com.spring.vsurin.bookexchange.domain.RequestStatus;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RequestAssembler extends RepresentationModelAssemblerSupport<Request, RequestDTO> {

    public RequestAssembler() {
        super(RequestService.class, RequestDTO.class);
    }

    @Override
    public RequestDTO toModel(Request request) {
        RequestDTO requestDTO = instantiateModel(request);

        requestDTO.setId(request.getId());
        requestDTO.setSenderId(request.getSender().getId());
        requestDTO.setReceiverId(request.getReceiver().getId());
        requestDTO.setBookSenderWantsId(request.getBookSenderWants().getId());
        if (request.getBookReceiverWants() != null)
            requestDTO.setBookReceiverWantsId(request.getBookReceiverWants().getId());
        requestDTO.setStatus(request.getStatus());
        requestDTO.setCommentForReceiver(request.getCommentForReceiver());

        requestDTO.add(linkTo(methodOn(RequestController.class).getRequestById(request.getId())).withSelfRel());

        return requestDTO;
    }
}
