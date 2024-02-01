package com.bank.service;

import com.bank.models.User;
import com.bank.props.MailProperties;
import com.bank.utils.enums.EmailType;
import freemarker.template.Configuration;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final Configuration configuration;
    private final MailProperties mailProperties;

    public void sendEmailMessage(User user, EmailType emailType, Properties properties){
        switch (emailType){
            case REGISTRATION -> sendRegistrationEmailMessage(user, properties);
            case PAYMENT -> sendPaymentEmailMessage(user, properties);
        }
    }

    @SneakyThrows
    public void sendRegistrationEmailMessage(User user, Properties properties){
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
        helper.setFrom(mailProperties.getUsername());
        helper.setTo(user.getEmail());
        helper.setSubject("Thank you for registration, dear" + user.getUsername());
        String emailContent = getEmailMessageForRegistration(user);
        helper.setText(emailContent, true);
        mailSender.send(message);
    }

    @SneakyThrows
    public void sendPaymentEmailMessage(User user, Properties properties){
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");
        helper.setFrom(mailProperties.getUsername());
        helper.setTo(user.getEmail());
        helper.setSubject("Bill " + user.getId());
        String emailContent = getEmailMessageForPayment(user);
        helper.setText(emailContent, true);
        mailSender.send(message);
    }

    @SneakyThrows
    public String getEmailMessageForRegistration(User user){
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("username", user.getUsername());
        model.put("code", user.getConfirmationCode());
        configuration.getTemplate("register.flth")
                .process(model, stringWriter);
        return stringWriter.getBuffer().toString();
    }

    @SneakyThrows
    public String getEmailMessageForPayment(User user){
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("username", user.getUsername());
        configuration.getTemplate("payment.flth")
                .process(model, stringWriter);
        return stringWriter.getBuffer().toString();
    }
}