package com.spring.vsurin.bookexchange.app;

import com.spring.vsurin.bookexchange.domain.Book;
import com.spring.vsurin.bookexchange.domain.EmailData;
import com.spring.vsurin.bookexchange.domain.Request;
import com.spring.vsurin.bookexchange.domain.User;

/**
 * Интерфейс для создания электронных сообщений.
 */
public interface MailBuilder {
    /**
     * Создаёт сообщение о создании запроса.
     *
     * @param receiver      Адрес электронной почты получателя.
     * @param requestId     Идентификатор запроса.
     * @param bookSenderWants Книга, которую отправитель хочет.
     * @return Объект EmailData, представляющий электронное сообщение.
     */
    EmailData buildCreateRequestMessage(String receiver, long requestId, Book bookSenderWants);

    /**
     * Создаёт сообщение о подтверждении запроса.
     *
     * @param receiver      Адрес электронной почты получателя.
     * @param relatedRequest Связанный запрос.
     * @return Объект EmailData, представляющий электронное сообщение.
     */
    EmailData buildAcceptRequestMessage(String receiver, Request relatedRequest);

    /**
     * Создаёт сообщение об отклонении запроса.
     *
     * @param receiver      Адрес электронной почты получателя.
     * @param requestId     Идентификатор запроса.
     * @return Объект EmailData, представляющий электронное сообщение.
     */
    EmailData buildRejectRequestMessage(String receiver, long requestId);

    /**
     * Создаёт сообщение об удалении пользователя.
     *
     * @param receiver      Адрес электронной почты получателя.
     * @param userId        Идентификатор пользователя.
     * @return Объект EmailData, представляющий электронное сообщение.
     */
    EmailData buildDeleteUserMessage(String receiver, long userId);

    /**
     * Создаёт сообщение с запросом на удаление пользователя.
     *
     * @param receiver      Адрес электронной почты получателя.
     * @param userId        Идентификатор пользователя.
     * @param reason        Причина удаления.
     * @return Объект EmailData, представляющий электронное сообщение.
     */
    EmailData buildRequestToDeleteUserMessage(String receiver, long userId, String reason);

    /**
     * Создаёт сообщение с жалобой.
     *
     * @param receiver              Адрес электронной почты получателя.
     * @param subjectId             Идентификатор объекта, на который направлена жалоба.
     * @param stringComplaintSubject    Тема жалобы.
     * @param complaint             Текст жалобы.
     * @return Объект EmailData, представляющий электронное сообщение.
     */
    EmailData buildSendComplaintMessage(String receiver, long subjectId, String stringComplaintSubject, String complaint);

    /**
     * Создаёт сообщение об обновлении трек-номера.
     *
     * @param receiver      Адрес электронной почты получателя.
     * @param exchangeId    Идентификатор обмена.
     * @param track         Статус отслеживания.
     * @return Объект EmailData, представляющий электронное сообщение.
     */
    EmailData buildUpdateTrackMessage(String receiver, long exchangeId, String track);

    /**
     * Создаёт сообщение об отсутствии трек-номера.
     *
     * @param receiver      Адрес электронной почты получателя.
     * @param exchangeId    Идентификатор обмена.
     * @return Объект EmailData, представляющий электронное сообщение.
     */
    EmailData buildNoTrackMessage(String receiver, long exchangeId);

    /**
     * Создаёт сообщение об установке статуса обмена "В процессе".
     *
     * @param receiver      Адрес электронной почты получателя.
     * @param exchangeId    Идентификатор обмена.
     * @return Объект EmailData, представляющий электронное сообщение.
     */
    EmailData buildSetInProgressStatusMessage(String receiver, long exchangeId);

    /**
     * Создаёт сообщение о получении книги.
     *
     * @param receiver      Адрес электронной почты получателя.
     * @param exchangeId    Идентификатор обмена.
     * @return Объект EmailData, представляющий электронное сообщение.
     */
    EmailData buildReceiveBookMessage(String receiver, long exchangeId);

    /**
     * Создаёт сообщение о завершении обмена.
     *
     * @param receiver      Адрес электронной почты получателя.
     * @param exchangeId    Идентификатор обмена.
     * @return Объект EmailData, представляющий электронное сообщение.
     */
    EmailData buildFinalizeExchangeMessage(String receiver, long exchangeId);

    /**
     * Создаёт сообщение о проблемах с обменом.
     *
     * @param receiver      Адрес электронной почты получателя.
     * @param exchangeId    Идентификатор обмена.
     * @param user          Пользователь, чьи контакты будут в письме.
     * @return Объект EmailData, представляющий электронное сообщение.
     */
    EmailData buildProblemsMessage(String receiver, long exchangeId, User user);
}
