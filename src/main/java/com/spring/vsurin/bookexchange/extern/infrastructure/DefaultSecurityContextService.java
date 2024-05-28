package com.spring.vsurin.bookexchange.extern.infrastructure;

import com.spring.vsurin.bookexchange.app.SecurityContextService;
import com.spring.vsurin.bookexchange.app.UserRepository;
import com.spring.vsurin.bookexchange.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class DefaultSecurityContextService implements SecurityContextService {

    private final UserRepository userRepository;

    public DefaultSecurityContextService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Получает идентификатор текущего аутентифицированного пользователя.
     *
     * @return Идентификатор текущего аутентифицированного пользователя
     * @throws IllegalArgumentException если пользователь не аутентифицирован или не найден в базе данных
     */
    public Long getCurrentAuthId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
            String name = oauth2User.getName();

            if (name != null) {
                User foundUser = userRepository.findByEmail(name);

                if (foundUser == null) {
                    throw new IllegalArgumentException("Пользователь с email " + name + " не найден");
                } else {
                    return foundUser.getId();
                }
            } else {
                throw new IllegalArgumentException("Имя пользователя равно null");
            }
        } else {
            throw new IllegalArgumentException("Пользователь не аутентифицирован или аутентификация не проведена через OAuth2");
        }
    }
}
