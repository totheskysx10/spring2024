package com.spring.vsurin.bookexchange.extern.infrastructure;

import com.spring.vsurin.bookexchange.app.MailBuilder;
import com.spring.vsurin.bookexchange.domain.Book;
import com.spring.vsurin.bookexchange.domain.EmailData;
import com.spring.vsurin.bookexchange.domain.Request;
import com.spring.vsurin.bookexchange.domain.User;
import org.springframework.stereotype.Component;

@Component
public class DefaultMailBuilder implements MailBuilder {

    public EmailData buildCreateRequestMessage(String receiver, long requestId, Book bookSenderWants) {
        String emailSubject = "BookExchange - Заявка на обмен";
        String emailMessage = "Вам отправлена заявка №" + requestId + " на обмен книгами. Отправитель хочет получить у вас книгу " +
                bookSenderWants.getAuthor() + " - " + bookSenderWants.getTitle() +
                ". Принять или отклонить заявку можно в приложении BookExchange.";

        return new EmailData(receiver, emailSubject, emailMessage);
    }

    public EmailData buildAcceptRequestMessage(String receiver, Request relatedRequest) {
        String emailSubject = "BookExchange - Заявка на обмен принята";
        String emailMessage = "Ваша заявка №" + relatedRequest.getId() + " на обмен книгами принята. Получатель заявки выбрал у вас книгу " +
                relatedRequest.getBookReceiverWants().getAuthor() + " - " + relatedRequest.getBookReceiverWants().getTitle() + ".";

        return new EmailData(receiver, emailSubject, emailMessage);
    }

    public EmailData buildRejectRequestMessage(String receiver, long requestId) {
        String emailSubject = "BookExchange - Заявка на обмен отклонена";
        String emailMessage = "Ваша заявка №" + requestId + " на обмен книгами отклонена.";

        return new EmailData(receiver, emailSubject, emailMessage);
    }

    public EmailData buildDeleteUserMessage(String receiver, long userId) {
        String emailSubject = "Аккаунт удалён";
        String emailMessage = "Добрый день! Удалён аккаунт пользователя с id " + userId;

        return new EmailData(receiver, emailSubject, emailMessage);
    }

    public EmailData buildRequestToDeleteUserMessage(String receiver, long userId, String reason) {
        String emailSubject = "Просьба удалить аккаунт";
        String emailMessage = "Добрый день! Прошу удалить мой аккаунт пользователя с id " + userId + ". Причина удаления: " + reason;

        return new EmailData(receiver, emailSubject, emailMessage);
    }

    public EmailData buildSendComplaintMessage(String receiver, long subjectId, String stringComplaintSubject, String complaint) {
        String emailSubject = "Жалоба от пользователя";
        String emailMessage = "Предмет жалобы: " + stringComplaintSubject + "\n" +
                "Id предмета жалобы: " + subjectId + "\n" +
                "Текст жалобы: " + complaint;

        return new EmailData(receiver, emailSubject, emailMessage);
    }

    public EmailData buildUpdateTrackMessage(String receiver, long exchangeId, String track) {
        String emailSubject = "BookExchange - Присвоен трек-номер обмену";
        String emailMessage = "Обмену книгами №" + exchangeId + " присвоен трек-номер: " + track + ".";

        return new EmailData(receiver, emailSubject, emailMessage);
    }

    public EmailData buildNoTrackMessage(String receiver, long exchangeId) {
        String emailSubject = "BookExchange - Обмен без трек-номера";
        String emailMessage = "Обмен книгами №" + exchangeId + " доставляется без трек-номера второго участника.";

        return new EmailData(receiver, emailSubject, emailMessage);
    }

    public EmailData buildSetInProgressStatusMessage(String receiver, long exchangeId) {
        String emailSubject = "BookExchange - Обмен в процессе доставки";
        String emailMessage = "Обмен книгами №" + exchangeId + " в процессе доставки.";

        return new EmailData(receiver, emailSubject, emailMessage);
    }

    public EmailData buildReceiveBookMessage(String receiver, long exchangeId) {
        String emailSubject = "BookExchange - Участник получил книгу";
        String emailMessage = "Обмен книгами №" + exchangeId + " - второй участник получил книгу.";

        return new EmailData(receiver, emailSubject, emailMessage);
    }

    public EmailData buildFinalizeExchangeMessage(String receiver, long exchangeId) {
        String emailSubject = "BookExchange - Обмен завершён";
        String emailMessage = "Обмен книгами №" + exchangeId + " успешно завершён. До встречи в BookExchange!";

        return new EmailData(receiver, emailSubject, emailMessage);
    }

    public EmailData buildProblemsMessage(String receiver, long exchangeId, User user) {
        String emailSubject = "BookExchange - Проблемы при обмене";

        String emailMessage = "В процессе обмена книгами №" + exchangeId + " возникли проблемы. Пожалуйста, свяжитесь с его вторым участником. Контакты:\n" +
                "Телефон: " + user.getPhoneNumber() + "\n" +
                "Почта: " + user.getEmail();

        return new EmailData(receiver, emailSubject, emailMessage);
    }

    public EmailData buildCancelMessage(String receiver, long exchangeId) {
        String emailSubject = "BookExchange - Обмен отменён админом";

        String emailMessage = "Обмен книгами №" + exchangeId + " отменён администратором приложения. Для уточнения подробностей свяжитесь с поддержкой.";

        return new EmailData(receiver, emailSubject, emailMessage);
    }
}
