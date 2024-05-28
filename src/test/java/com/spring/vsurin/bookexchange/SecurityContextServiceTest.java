package com.spring.vsurin.bookexchange;

import com.spring.vsurin.bookexchange.app.SecurityContextService;
import com.spring.vsurin.bookexchange.app.UserRepository;
import com.spring.vsurin.bookexchange.domain.User;
import com.spring.vsurin.bookexchange.domain.UserGender;
import com.spring.vsurin.bookexchange.domain.UserRole;
import com.spring.vsurin.bookexchange.extern.infrastructure.DefaultSecurityContextService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@DataJpaTest
public class SecurityContextServiceTest {

    @InjectMocks
    private DefaultSecurityContextService securityContextService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OAuth2User oauth2User;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @Test
    public void testGetCurrentAuthId() {
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
                .build();

        when(userRepository.findByEmail("min0@list.ru")).thenReturn(user);
        when(oauth2User.getName()).thenReturn("min0@list.ru");
        when(authentication.getPrincipal()).thenReturn(oauth2User);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        Long id = securityContextService.getCurrentAuthId();
        assertEquals(user.getId(), id);
    }
}
