package com.spring.vsurin.bookexchange.app;

/**
 * Интерфейс для отправки электронной почты.
 */
public interface EmailService {

    /**
     * Отправляет электронное письмо на указанный адрес с заданной темой и сообщением.
     *
     * @param address адрес электронной почты получателя
     * @param subject тема письма
     * @param message содержание письма
     */
    void sendEmail(String address, String subject, String message);
}
