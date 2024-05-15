package com.spring.vsurin.bookexchange.extern.api;

import com.spring.vsurin.bookexchange.domain.UserGender;
import com.spring.vsurin.bookexchange.domain.UserRole;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
public class UserDTO extends RepresentationModel<UserDTO> {

    private long id;

    @NotNull
    @NotEmpty
    @Size(max = 300)
    private String username;

    private UserGender gender;

    @Email(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    private String email;

    private List<Long> libraryIds;

    private List<Long> offeredBookIds;

    private List<String> addressList;

    @Pattern(regexp = "^\\+[0-9]{1,3}[0-9]{0,10}$")
    private String phoneNumber;

    private List<Long> exchangesIdsAsMember1;

    private List<Long> exchangesIdsAsMember2;

    @NotNull
    private UserRole role;

    @Size(min = 50)
    private String mainAddress;

    private boolean showContacts;

    private String avatarLink;

    @Size(max = 1000)
    private String preferences;

    private Iterable<Long> usersWithAccessToMainAddress;
}
