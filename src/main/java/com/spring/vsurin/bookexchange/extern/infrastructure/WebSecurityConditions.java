package com.spring.vsurin.bookexchange.extern.infrastructure;

import com.spring.vsurin.bookexchange.app.UserRepository;
import com.spring.vsurin.bookexchange.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

/**
 * Класс, проверяющий условия для конфигурации Spring Security.
 */
@Slf4j
@Component
public class WebSecurityConditions {

    private final UserRepository userRepository;

    public WebSecurityConditions(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Проверяет, является ли текущий аутентифицированный пользователь владельцем указанного идентификатора.
     * Если пользователь не авторизован, то userId будет сравниваться с нулём, т.е. вернётся false, т.к. счёт в БД ведётся с единицы.
     *
     * @param userId идентификатор пользователя, с которым сравнивается текущий пользователь
     * @return true, если текущий пользователь аутентифицирован и его идентификатор совпадает с указанным userId; false в противном случае
     */
    public boolean isCurrentUser(long userId) {
        long currentId = 0;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof OAuth2User) {
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
            String name = oauth2User.getName();
            if (name != null) {
                User currentUser = userRepository.findByEmail(name);
                currentId = currentUser.getId();
            }
            else
                log.error("Пользователь не найден!");
        }
        else
            log.error("Отсутствуют данные об аутентификации!");

        return currentId == userId;
    }
}
