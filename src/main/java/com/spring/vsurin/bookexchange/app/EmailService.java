package com.spring.vsurin.bookexchange.app;

import com.spring.vsurin.bookexchange.domain.EmailData;

/**
 * Интерфейс для отправки электронной почты.
 */
public interface EmailService {

    /**
     * Отправляет электронное письмо на указанный адрес с заданной темой и сообщением.
     *
     * @param emailData данные для отправки сообщения
     */
    void sendEmail(EmailData emailData);
}
