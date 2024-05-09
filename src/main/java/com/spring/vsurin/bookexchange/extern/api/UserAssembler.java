package com.spring.vsurin.bookexchange.extern.api;

import com.spring.vsurin.bookexchange.domain.Book;
import com.spring.vsurin.bookexchange.domain.Exchange;
import com.spring.vsurin.bookexchange.domain.User;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserAssembler extends RepresentationModelAssemblerSupport<User, UserDTO> {

    public UserAssembler() {
        super(UserController.class, UserDTO.class);
    }

    @Override
    public UserDTO toModel(User user) {
        UserDTO userDTO = instantiateModel(user);

        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setGender(user.getGender());
        userDTO.setEmail(user.getEmail());
        userDTO.setLibraryIds(user.getLibrary().stream()
                .map(Book::getId)
                .collect(Collectors.toList()));
        userDTO.setOfferedBookIds(user.getOfferedBooks().stream()
                .map(Book::getId)
                .collect(Collectors.toList()));
        userDTO.setAddressList(user.getAddressList());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setExchangesIdsAsMember1(user.getExchangesAsMember1().stream()
                .map(Exchange::getId)
                .collect(Collectors.toList()));
        userDTO.setExchangesIdsAsMember2(user.getExchangesAsMember2().stream()
                .map(Exchange::getId)
                .collect(Collectors.toList()));
        userDTO.setMainAddress(user.getMainAddress());
        userDTO.setRole(user.getRole());
        userDTO.setShowContacts(user.isShowContacts());
        userDTO.setAvatarLink(user.getAvatarLink());
        userDTO.setPreferences(user.getPreferences());

        userDTO.add(linkTo(methodOn(UserController.class).getUserById(user.getId())).withSelfRel());

        return userDTO;
    }
}

