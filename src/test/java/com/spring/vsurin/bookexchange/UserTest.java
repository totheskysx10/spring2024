package com.spring.vsurin.bookexchange;

import com.spring.vsurin.bookexchange.app.UserRepository;
import com.spring.vsurin.bookexchange.domain.User;
import com.spring.vsurin.bookexchange.domain.UserGender;
import com.spring.vsurin.bookexchange.domain.UserRole;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@DataJpaTest
public class UserTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OAuth2User oauth2User;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Test
    public void testGetAddressList() {
        User user = User.builder()
                .id(1)
                .email("min0@list.ru")
                .username("us")
                .role(UserRole.ROLE_USER)
                .gender(UserGender.MALE)
                .showContacts(true)
                .phoneNumber("+79123456789")
                .avatarLink("Link")
                .preferences("one")
                .addressList(new ArrayList<>())
                .build();

        when(userRepository.findById(1)).thenReturn(user);
        when(oauth2User.getName()).thenReturn("min0@list.ru");
        when(authentication.getPrincipal()).thenReturn(oauth2User);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        assertEquals(user.getAddressList(), new ArrayList<>());
    }

    @Test
    public void testGetAddressListWithOtherAuth() {
        User user = User.builder()
                .id(1)
                .email("min0@list.ru")
                .username("us")
                .role(UserRole.ROLE_USER)
                .gender(UserGender.MALE)
                .showContacts(true)
                .phoneNumber("+79123456789")
                .avatarLink("Link")
                .preferences("one")
                .addressList(new ArrayList<>())
                .build();

        when(userRepository.findById(1)).thenReturn(user);
        when(oauth2User.getName()).thenReturn("min01@list.ru");
        when(authentication.getPrincipal()).thenReturn(oauth2User);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        assertEquals(user.getAddressList(), null);
    }

    @Test
    public void testGetPhoneNumber() {
        User user = User.builder()
                .id(1)
                .email("min0@list.ru")
                .username("us")
                .role(UserRole.ROLE_USER)
                .gender(UserGender.MALE)
                .showContacts(true)
                .phoneNumber("+79123456789")
                .avatarLink("Link")
                .preferences("one")
                .addressList(new ArrayList<>())
                .build();

        when(userRepository.findById(1)).thenReturn(user);

        assertEquals(user.getPhoneNumber(), "+79123456789");
    }

    @Test
    public void testGetPhoneNumberWitNoAccess() {
        User user = User.builder()
                .id(1)
                .email("min0@list.ru")
                .username("us")
                .role(UserRole.ROLE_USER)
                .gender(UserGender.MALE)
                .showContacts(false)
                .phoneNumber("+79123456789")
                .avatarLink("Link")
                .preferences("one")
                .addressList(new ArrayList<>())
                .build();

        when(userRepository.findById(1)).thenReturn(user);

        assertEquals(user.getPhoneNumber(), null);
    }

    @Test
    public void testAddUserWithAccessToMainAddress() {
        User user = User.builder()
                .id(1)
                .email("min0@list.ru")
                .username("us")
                .role(UserRole.ROLE_USER)
                .gender(UserGender.MALE)
                .showContacts(true)
                .phoneNumber("+79123456789")
                .avatarLink("Link")
                .preferences("one")
                .usersWithAccessToMainAddress(new ArrayList<>())
                .build();

        when(userRepository.findById(1)).thenReturn(user);

        user.addUserWithAccessToMainAddress(2);
        int i = 0;

        for (long id : user.getUsersWithAccessToMainAddress()) {
            i++;
        }
        assertEquals(1, i);
    }

    @Test
    public void testRemoveUserWithAccessToMainAddress() {
        User user = User.builder()
                .id(1)
                .email("min0@list.ru")
                .username("us")
                .role(UserRole.ROLE_USER)
                .gender(UserGender.MALE)
                .showContacts(true)
                .phoneNumber("+79123456789")
                .avatarLink("Link")
                .preferences("one")
                .usersWithAccessToMainAddress(new ArrayList<>())
                .build();

        when(userRepository.findById(1)).thenReturn(user);

        user.addUserWithAccessToMainAddress(2);
        user.addUserWithAccessToMainAddress(3);
        user.addUserWithAccessToMainAddress(4);
        user.addUserWithAccessToMainAddress(1);
        user.addUserWithAccessToMainAddress(2);

        user.removeUserWithAccessToMainAddress(2);

        int count = 0;
        long firstId = 0;

        for (long id : user.getUsersWithAccessToMainAddress()) {
            if (count == 0) {
                firstId = id;
            }
            count++;
        }

        assertEquals(4, count);
        assertEquals(3, firstId);
    }

    @Test
    public void testGetMainAddressByMyself() {
        User user = User.builder()
                .id(1)
                .email("min0@list.ru")
                .username("us")
                .role(UserRole.ROLE_USER)
                .gender(UserGender.MALE)
                .showContacts(true)
                .mainAddress("add")
                .phoneNumber("+79123456789")
                .avatarLink("Link")
                .preferences("one")
                .usersWithAccessToMainAddress(new ArrayList<>())
                .build();

        when(userRepository.findById(1)).thenReturn(user);
        when(oauth2User.getName()).thenReturn("min0@list.ru");
        when(authentication.getPrincipal()).thenReturn(oauth2User);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        assertEquals("add", user.getMainAddress(1));
    }

    @Test
    public void testGetMainAddressByOther() {
        User user = User.builder()
                .id(1)
                .email("min0@list.ru")
                .username("us")
                .role(UserRole.ROLE_USER)
                .gender(UserGender.MALE)
                .showContacts(true)
                .mainAddress("add")
                .phoneNumber("+79123456789")
                .avatarLink("Link")
                .preferences("one")
                .usersWithAccessToMainAddress(new ArrayList<>())
                .build();

        when(userRepository.findById(1)).thenReturn(user);

        assertEquals(null, user.getMainAddress(2));
    }

    @Test
    public void testGetMainAddressByOtherWithAccess() {
        User user = User.builder()
                .id(1)
                .email("min0@list.ru")
                .username("us")
                .role(UserRole.ROLE_USER)
                .gender(UserGender.MALE)
                .showContacts(true)
                .mainAddress("add")
                .phoneNumber("+79123456789")
                .avatarLink("Link")
                .preferences("one")
                .usersWithAccessToMainAddress(new ArrayList<>())
                .build();

        when(userRepository.findById(1)).thenReturn(user);
        user.addUserWithAccessToMainAddress(2);

        assertEquals("add", user.getMainAddress(2));
    }
}
