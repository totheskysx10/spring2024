package com.spring.vsurin.bookexchange.app;

/**
 * Интерфейс для получения контекста безопасности.
 */
public interface SecurityContextService {

    /**
     * Получает идентификатор текущего аутентифицированного пользователя.
     *
     * @return Идентификатор текущего аутентифицированного пользователя
     * @throws IllegalArgumentException если пользователь не аутентифицирован или не найден в базе данных
     */
    Long getCurrentAuthId();
}

