package com.spring.vsurin.bookexchange;

import com.spring.vsurin.bookexchange.app.ComplaintService;
import com.spring.vsurin.bookexchange.app.EmailService;
import com.spring.vsurin.bookexchange.app.MailBuilder;
import com.spring.vsurin.bookexchange.app.UserRepository;
import com.spring.vsurin.bookexchange.domain.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DataJpaTest
public class ComplaintServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ComplaintService complaintService;

    @Mock
    private EmailService emailService;

    @Mock
    private MailBuilder mailBuilder;

    @Test
    public void testSendComplaint() {
        User admin1 = User.builder()
                .id(1)
                .email("min095@list.ru")
                .username("ad")
                .role(UserRole.ROLE_ADMIN)
                .gender(UserGender.MALE)
                .build();

        User admin2 = User.builder()
                .id(2)
                .email("min095@list.ru")
                .username("ad")
                .role(UserRole.ROLE_ADMIN)
                .gender(UserGender.MALE)
                .build();

        List<User> admins = new ArrayList<>();
        admins.add(admin1);
        admins.add(admin2);

        User user = User.builder()
                .id(3)
                .email("min0@list.ru")
                .username("us")
                .role(UserRole.ROLE_USER)
                .gender(UserGender.MALE)
                .build();

        when(userRepository.findByRole(UserRole.ROLE_ADMIN)).thenReturn(admins);
        when(userRepository.findById(3)).thenReturn(user);
        when(mailBuilder.buildSendComplaintMessage(anyString(), anyLong(), anyString(), anyString()))
                .thenReturn(new EmailData("min095@list.ru", "Subject", "Message"));

        complaintService.sendComplaint(ComplaintSubject.BOOK, 10, anyString());

        verify(emailService, times(2)).sendEmail(eq("min095@list.ru"), anyString(), anyString());
    }
}
