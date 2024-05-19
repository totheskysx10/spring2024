package com.spring.vsurin.bookexchange;

import com.spring.vsurin.bookexchange.app.*;
import com.spring.vsurin.bookexchange.domain.Book;
import com.spring.vsurin.bookexchange.domain.EmailData;
import com.spring.vsurin.bookexchange.domain.Request;
import com.spring.vsurin.bookexchange.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.Assert.assertEquals;

@Sql("/with_exchanges.sql")
@SpringBootTest
public class MailBuilderTest {

    @Autowired
    private MailBuilder mailBuilder;

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @Autowired
    private RequestService requestService;

    @Test
    public void testBuildCreateRequestMessage() {

        String receiver = "recipient@example.com";
        long requestId = 123;
        Book book = bookService.getBookById(1);

        EmailData emailData = mailBuilder.buildCreateRequestMessage(receiver, requestId, book);

        assertEquals("BookExchange - Заявка на обмен", emailData.getEmailSubject());
        assertEquals("Вам отправлена заявка №123 на обмен книгами. " +
                "Отправитель хочет получить у вас книгу Test Author 1 - Test Book 1. " +
                "Принять или отклонить заявку можно в приложении BookExchange.", emailData.getEmailMessage());
        assertEquals("recipient@example.com", emailData.getEmailReceiver());
    }

    @Sql("/with_req.sql")
    @Test
    public void testBuildAcceptRequestMessage() {
        String receiver = "recipient@example.com";
        Book book = bookService.getBookById(1);
        Request request = requestService.getRequestById(1);
        request.setBookReceiverWants(book);

        EmailData emailData = mailBuilder.buildAcceptRequestMessage(receiver, request);

        assertEquals("BookExchange - Заявка на обмен принята", emailData.getEmailSubject());
        assertEquals("Ваша заявка №1 на обмен книгами принята. " +
                "Получатель заявки выбрал у вас книгу Test Author 1 - Test Book 1.", emailData.getEmailMessage());
        assertEquals("recipient@example.com", emailData.getEmailReceiver());
    }

    @Test
    public void testBuildRejectRequestMessage() {
        String receiver = "recipient@example.com";
        long requestId = 789;

        EmailData emailData = mailBuilder.buildRejectRequestMessage(receiver, requestId);

        assertEquals("BookExchange - Заявка на обмен отклонена", emailData.getEmailSubject());
        assertEquals("Ваша заявка №789 на обмен книгами отклонена.", emailData.getEmailMessage());
        assertEquals("recipient@example.com", emailData.getEmailReceiver());
    }

    @Test
    public void testBuildDeleteUserMessage() {
        String receiver = "admin@example.com";
        long userId = 987;

        EmailData emailData = mailBuilder.buildDeleteUserMessage(receiver, userId);

        assertEquals("Аккаунт удалён", emailData.getEmailSubject());
        assertEquals("Добрый день! Удалён аккаунт пользователя с id 987", emailData.getEmailMessage());
        assertEquals("admin@example.com", emailData.getEmailReceiver());
    }

    @Test
    public void testBuildRequestToDeleteUserMessage() {
        String receiver = "admin@example.com";
        long userId = 654;
        String reason = "Не пользуюсь сервисом больше";

        EmailData emailData = mailBuilder.buildRequestToDeleteUserMessage(receiver, userId, reason);

        assertEquals("Просьба удалить аккаунт", emailData.getEmailSubject());
        assertEquals("Добрый день! Прошу удалить мой аккаунт пользователя с id 654. Причина удаления: Не пользуюсь сервисом больше", emailData.getEmailMessage());
        assertEquals("admin@example.com", emailData.getEmailReceiver());
    }

    @Test
    public void testBuildSendComplaintMessage() {
        String receiver = "admin@example.com";
        long subjectId = 321;
        String complaintSubject = "Процесс обмена";
        String complaint = "Меня обманули.";

        EmailData emailData = mailBuilder.buildSendComplaintMessage(receiver, subjectId, complaintSubject, complaint);

        assertEquals("Жалоба от пользователя", emailData.getEmailSubject());
        assertEquals("Предмет жалобы: Процесс обмена\n" +
                "Id предмета жалобы: 321\n" +
                "Текст жалобы: Меня обманули.", emailData.getEmailMessage());
        assertEquals("admin@example.com", emailData.getEmailReceiver());
    }

    @Test
    public void testBuildUpdateTrackMessage() {
        String receiver = "recipient@example.com";
        long exchangeId = 111;
        String track = "123456789";

        EmailData emailData = mailBuilder.buildUpdateTrackMessage(receiver, exchangeId, track);

        assertEquals("BookExchange - Присвоен трек-номер обмену", emailData.getEmailSubject());
        assertEquals("Обмену книгами №111 присвоен трек-номер: 123456789.", emailData.getEmailMessage());
        assertEquals("recipient@example.com", emailData.getEmailReceiver());
    }

    @Test
    public void testBuildNoTrackMessage() {
        String receiver = "recipient@example.com";
        long exchangeId = 222;

        EmailData emailData = mailBuilder.buildNoTrackMessage(receiver, exchangeId);

        assertEquals("BookExchange - Обмен без трек-номера", emailData.getEmailSubject());
        assertEquals("Обмен книгами №222 доставляется без трек-номера второго участника.", emailData.getEmailMessage());
        assertEquals("recipient@example.com", emailData.getEmailReceiver());
    }

    @Test
    public void testBuildSetInProgressStatusMessage() {
        String receiver = "recipient@example.com";
        long exchangeId = 333;

        EmailData emailData = mailBuilder.buildSetInProgressStatusMessage(receiver, exchangeId);

        assertEquals("BookExchange - Обмен в процессе доставки", emailData.getEmailSubject());
        assertEquals("Обмен книгами №333 в процессе доставки.", emailData.getEmailMessage());
        assertEquals("recipient@example.com", emailData.getEmailReceiver());
    }

    @Test
    public void testBuildReceiveBookMessage() {
        String receiver = "recipient@example.com";
        long exchangeId = 444;

        EmailData emailData = mailBuilder.buildReceiveBookMessage(receiver, exchangeId);

        assertEquals("BookExchange - Участник получил книгу", emailData.getEmailSubject());
        assertEquals("Обмен книгами №444 - второй участник получил книгу.", emailData.getEmailMessage());
        assertEquals("recipient@example.com", emailData.getEmailReceiver());
    }

    @Test
    public void testBuildFinalizeExchangeMessage() {
        String receiver = "recipient@example.com";
        long exchangeId = 555;

        EmailData emailData = mailBuilder.buildFinalizeExchangeMessage(receiver, exchangeId);

        assertEquals("BookExchange - Обмен завершён", emailData.getEmailSubject());
        assertEquals("Обмен книгами №555 успешно завершён. До встречи в BookExchange!", emailData.getEmailMessage());
        assertEquals("recipient@example.com", emailData.getEmailReceiver());
    }

    @Test
    public void testBuildProblemsMessage() {
        String receiver = "recipient@example.com";
        long exchangeId = 666;
        userService.enableShowContacts(1);
        User user = userService.getUserById(1);

        EmailData emailData = mailBuilder.buildProblemsMessage(receiver, exchangeId, user);

        assertEquals("BookExchange - Проблемы при обмене", emailData.getEmailSubject());
        assertEquals("В процессе обмена книгами №666 возникли проблемы. " +
                "Пожалуйста, свяжитесь с его вторым участником. Контакты:\n" +
                "Телефон: 89000000000\n" +
                "Почта: test1@example.com", emailData.getEmailMessage());
        assertEquals("recipient@example.com", emailData.getEmailReceiver());
    }

    @Test
    public void testBuildCancelMessage() {
        String receiver = "recipient@example.com";
        long exchangeId = 555;

        EmailData emailData = mailBuilder.buildCancelMessage(receiver, exchangeId);

        assertEquals("BookExchange - Обмен отменён админом", emailData.getEmailSubject());
        assertEquals("Обмен книгами №555 отменён администратором приложения. Для уточнения подробностей свяжитесь с поддержкой.", emailData.getEmailMessage());
        assertEquals("recipient@example.com", emailData.getEmailReceiver());
    }

    @Test
    public void testBuildAvailableFromWishlistMessage() {
        String receiver = "recipient@example.com";
        String author = "Author";
        String title = "Title";
        String name = "Username";

        EmailData emailData = mailBuilder.buildAvailableFromWishlistMessage(receiver, title, author, name);

        assertEquals("BookExchange - Книга из списка желаний доступна для обмена", emailData.getEmailSubject());
        assertEquals("Книга Author - Title из вашего списка желаний доступна для обмена. Её предлагает пользователь Username.", emailData.getEmailMessage());
        assertEquals("recipient@example.com", emailData.getEmailReceiver());
    }
}

