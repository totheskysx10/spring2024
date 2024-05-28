package com.spring.vsurin.bookexchange.extern.infrastructure;

import com.spring.vsurin.bookexchange.app.SecurityContextService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Класс, проверяющий условия для конфигурации Spring Security.
 */
@Slf4j
@Component
public class WebSecurityConditions  {

    private final SecurityContextService securityContextService;

    public WebSecurityConditions(SecurityContextService securityContextService) {
        this.securityContextService = securityContextService;
    }

    /**
     * Проверяет, является ли текущий аутентифицированный пользователь владельцем указанного идентификатора.
     *
     * @param userId идентификатор пользователя, с которым сравнивается текущий пользователь
     * @return true, если текущий пользователь аутентифицирован и его идентификатор совпадает с указанным userId; false в противном случае
     */
    public boolean isCurrentUser(long userId) {
        return securityContextService.getCurrentAuthId() == userId;
    }
}
