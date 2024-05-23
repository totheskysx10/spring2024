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

        assertEquals("BookExchange - ������ �� �����", emailData.getEmailSubject());
        assertEquals("��� ���������� ������ �123 �� ����� �������. " +
                "����������� ����� �������� � ��� ����� Test Author 1 - Test Book 1. " +
                "������� ��� ��������� ������ ����� � ���������� BookExchange.", emailData.getEmailMessage());
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

        assertEquals("BookExchange - ������ �� ����� �������", emailData.getEmailSubject());
        assertEquals("���� ������ �1 �� ����� ������� �������. " +
                "���������� ������ ������ � ��� ����� Test Author 1 - Test Book 1.", emailData.getEmailMessage());
        assertEquals("recipient@example.com", emailData.getEmailReceiver());
    }

    @Test
    public void testBuildRejectRequestMessage() {
        String receiver = "recipient@example.com";
        long requestId = 789;

        EmailData emailData = mailBuilder.buildRejectRequestMessage(receiver, requestId);

        assertEquals("BookExchange - ������ �� ����� ���������", emailData.getEmailSubject());
        assertEquals("���� ������ �789 �� ����� ������� ���������.", emailData.getEmailMessage());
        assertEquals("recipient@example.com", emailData.getEmailReceiver());
    }

    @Test
    public void testBuildDeleteUserMessage() {
        String receiver = "admin@example.com";
        long userId = 987;

        EmailData emailData = mailBuilder.buildDeleteUserMessage(receiver, userId);

        assertEquals("������� �����", emailData.getEmailSubject());
        assertEquals("������ ����! ����� ������� ������������ � id 987", emailData.getEmailMessage());
        assertEquals("admin@example.com", emailData.getEmailReceiver());
    }

    @Test
    public void testBuildRequestToDeleteUserMessage() {
        String receiver = "admin@example.com";
        long userId = 654;
        String reason = "�� ��������� �������� ������";

        EmailData emailData = mailBuilder.buildRequestToDeleteUserMessage(receiver, userId, reason);

        assertEquals("������� ������� �������", emailData.getEmailSubject());
        assertEquals("������ ����! ����� ������� ��� ������� ������������ � id 654. ������� ��������: �� ��������� �������� ������", emailData.getEmailMessage());
        assertEquals("admin@example.com", emailData.getEmailReceiver());
    }

    @Test
    public void testBuildSendComplaintMessage() {
        String receiver = "admin@example.com";
        long subjectId = 321;
        String complaintSubject = "������� ������";
        String complaint = "���� ��������.";

        EmailData emailData = mailBuilder.buildSendComplaintMessage(receiver, subjectId, complaintSubject, complaint);

        assertEquals("������ �� ������������", emailData.getEmailSubject());
        assertEquals("������� ������: ������� ������\n" +
                "Id �������� ������: 321\n" +
                "����� ������: ���� ��������.", emailData.getEmailMessage());
        assertEquals("admin@example.com", emailData.getEmailReceiver());
    }

    @Test
    public void testBuildUpdateTrackMessage() {
        String receiver = "recipient@example.com";
        long exchangeId = 111;
        String track = "123456789";

        EmailData emailData = mailBuilder.buildUpdateTrackMessage(receiver, exchangeId, track);

        assertEquals("BookExchange - �������� ����-����� ������", emailData.getEmailSubject());
        assertEquals("������ ������� �111 �������� ����-�����: 123456789.", emailData.getEmailMessage());
        assertEquals("recipient@example.com", emailData.getEmailReceiver());
    }

    @Test
    public void testBuildNoTrackMessage() {
        String receiver = "recipient@example.com";
        long exchangeId = 222;

        EmailData emailData = mailBuilder.buildNoTrackMessage(receiver, exchangeId);

        assertEquals("BookExchange - ����� ��� ����-������", emailData.getEmailSubject());
        assertEquals("����� ������� �222 ������������ ��� ����-������ ������� ���������.", emailData.getEmailMessage());
        assertEquals("recipient@example.com", emailData.getEmailReceiver());
    }

    @Test
    public void testBuildSetInProgressStatusMessage() {
        String receiver = "recipient@example.com";
        long exchangeId = 333;

        EmailData emailData = mailBuilder.buildSetInProgressStatusMessage(receiver, exchangeId);

        assertEquals("BookExchange - ����� � �������� ��������", emailData.getEmailSubject());
        assertEquals("����� ������� �333 � �������� ��������.", emailData.getEmailMessage());
        assertEquals("recipient@example.com", emailData.getEmailReceiver());
    }

    @Test
    public void testBuildReceiveBookMessage() {
        String receiver = "recipient@example.com";
        long exchangeId = 444;

        EmailData emailData = mailBuilder.buildReceiveBookMessage(receiver, exchangeId);

        assertEquals("BookExchange - �������� ������� �����", emailData.getEmailSubject());
        assertEquals("����� ������� �444 - ������ �������� ������� �����.", emailData.getEmailMessage());
        assertEquals("recipient@example.com", emailData.getEmailReceiver());
    }

    @Test
    public void testBuildFinalizeExchangeMessage() {
        String receiver = "recipient@example.com";
        long exchangeId = 555;

        EmailData emailData = mailBuilder.buildFinalizeExchangeMessage(receiver, exchangeId);

        assertEquals("BookExchange - ����� ��������", emailData.getEmailSubject());
        assertEquals("����� ������� �555 ������� ��������. �� ������� � BookExchange!", emailData.getEmailMessage());
        assertEquals("recipient@example.com", emailData.getEmailReceiver());
    }

    @Test
    public void testBuildProblemsMessage() {
        String receiver = "recipient@example.com";
        long exchangeId = 666;
        userService.enableShowContacts(1);
        User user = userService.getUserById(1);

        EmailData emailData = mailBuilder.buildProblemsMessage(receiver, exchangeId, user);

        assertEquals("BookExchange - �������� ��� ������", emailData.getEmailSubject());
        assertEquals("� �������� ������ ������� �666 �������� ��������. " +
                "����������, ��������� � ��� ������ ����������. ��������:\n" +
                "�������: 89000000000\n" +
                "�����: test1@example.com", emailData.getEmailMessage());
        assertEquals("recipient@example.com", emailData.getEmailReceiver());
    }

    @Test
    public void testBuildCancelMessage() {
        String receiver = "recipient@example.com";
        long exchangeId = 555;

        EmailData emailData = mailBuilder.buildCancelMessage(receiver, exchangeId);

        assertEquals("BookExchange - ����� ������ �������", emailData.getEmailSubject());
        assertEquals("����� ������� �555 ������ ��������������� ����������. ��� ��������� ������������ ��������� � ����������.", emailData.getEmailMessage());
        assertEquals("recipient@example.com", emailData.getEmailReceiver());
    }

    @Test
    public void testBuildAvailableFromWishlistMessage() {
        String receiver = "recipient@example.com";
        String author = "Author";
        String title = "Title";
        String name = "Username";

        EmailData emailData = mailBuilder.buildAvailableFromWishlistMessage(receiver, title, author, name);

        assertEquals("BookExchange - ����� �� ������ ������� �������� ��� ������", emailData.getEmailSubject());
        assertEquals("����� Author - Title �� ������ ������ ������� �������� ��� ������. Ÿ ���������� ������������ Username.", emailData.getEmailMessage());
        assertEquals("recipient@example.com", emailData.getEmailReceiver());
    }
}

