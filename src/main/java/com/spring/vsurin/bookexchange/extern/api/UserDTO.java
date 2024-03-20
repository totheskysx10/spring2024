package com.spring.vsurin.bookexchange.extern.api;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
public class UserDTO extends RepresentationModel<UserDTO> {
    private long id;
    private String username;
    private String email;
    private List<Long> libraryIds;
    private List<Long> offeredBookIds;
    private List<String> addressList;
    private String phoneNumber;
    private List<Long> exchangesIdsAsMember1;
    private List<Long> exchangesIdsAsMember2;
    private String mainAddress;
}
