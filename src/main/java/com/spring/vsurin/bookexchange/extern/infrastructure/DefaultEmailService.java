package com.spring.vsurin.bookexchange.extern.infrastructure;

import com.spring.vsurin.bookexchange.app.EmailService;
import com.spring.vsurin.bookexchange.domain.EmailData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class DefaultEmailService implements EmailService {

    @Value("${spring.mail.username}")
    private String emailFrom;
    private final JavaMailSender javaMailSender;

    public DefaultEmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(EmailData emailData) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(emailFrom);
            simpleMailMessage.setTo(emailData.getEmailReceiver());
            simpleMailMessage.setSubject(emailData.getEmailSubject());
            simpleMailMessage.setText(emailData.getEmailMessage());
            javaMailSender.send(simpleMailMessage);
        } catch (MailException e) {
            e.printStackTrace();
        }
    }
}
