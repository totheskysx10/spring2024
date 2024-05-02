package com.spring.vsurin.bookexchange.app;

import com.spring.vsurin.bookexchange.domain.ComplaintSubject;
import com.spring.vsurin.bookexchange.domain.User;
import com.spring.vsurin.bookexchange.domain.UserRole;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Сервисный класс для отправки жалоб.
 */
@Component
public class ComplaintService {

    private final EmailService emailService;
    private final UserRepository userRepository;

    public ComplaintService(EmailService emailService, UserRepository userRepository) {
        this.emailService = emailService;
        this.userRepository = userRepository;
    }

    /**
     * Отправляет жалобу с указанными данными администраторам.
     *
     * @param complaintSubject объект жалобы (BOOK, USER или EXCHANGE)
     * @param subjectId         идентификатор объекта жалобы
     * @param complaint        текст жалобы
     */
    public void sendComplaint(ComplaintSubject complaintSubject, long subjectId, String complaint) {
        List<User> adminList = userRepository.findByRole(UserRole.ROLE_ADMIN);

        String stringComplaintSubject = null;

        switch (complaintSubject) {
            case BOOK:
                stringComplaintSubject = "Книга";
                break;
            case USER:
                stringComplaintSubject = "Пользователь";
                break;
            case EXCHANGE:
                stringComplaintSubject = "Процесс обмена";
                break;
        }

        String emailSubject = "Жалоба от пользователя";
        String emailMessage = "Предмет жалобы: " + stringComplaintSubject + "\n" +
                "Id предмета жалобы: " + subjectId + "\n" +
                "Текст жалобы: " + complaint;


        adminList.stream()
                .map(User::getEmail)
                .forEach(emailReceiver -> emailService.sendEmail(emailReceiver, emailSubject, emailMessage));
    }
}
