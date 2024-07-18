package com.map.moloapi.services;

import com.map.moloapi.contracts.notifications.Attachment;
import com.map.moloapi.contracts.notifications.EmailRequest;
import com.map.moloapi.entities.User;
import com.map.moloapi.utils.Utilities;
import com.map.moloapi.utils.constants.TechnicalMessage;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

/**
 * @author DIOMANDE Souleymane 
 * @project socoprim-internal-api
 * @Date 20/02/2024 11:56
 */
@Service
@Slf4j
public class NotificationService {

//    private final JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private SpringTemplateEngine templateEngine;
    @Autowired
    Utilities utilities;
    @Value("${account-creation-template}")
    String accountCreationTemplate;
    @Value("${account-reset-password-template}")
    String accountResetPasswordTemplate;
    @Value("${account-unlock-template}")
    String accountUnlockTemplate;

    //    @Override
    public void sendSimpleMail() throws MessagingException {
        log.info("-- Debut envoi mail test --");
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        helper.setTo("sdiomande001@gmail.com");
        helper.setText("Juste un message de test Socoprim internal - " + Utilities.now());
        helper.setSubject("TEST");
        helper.setFrom(utilities.getParam("FROM_MAIL"));
        javaMailSender.send(message);
        log.info("-- Fin envoi mail test --");
    }


    //    @Override
    public void sendMail(EmailRequest detail) throws MessagingException {
        log.info("-- Debut envoi mail --");
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        Context context = new Context();

        if (detail.getAttachments() != null && !detail.getAttachments().isEmpty()) {
            for (Attachment attachment : detail.getAttachments()) {
                helper.addAttachment(attachment.getName(), attachment.getFile());
            }
        }
        detail.setFooter(utilities.getParam("EMAIL_FOOTER"));
        context.setVariable("email", detail);

        String html = templateEngine.process(detail.getTemplate(), context);

        helper.setTo(detail.getTo());
        helper.setText(html, true);
        helper.setSubject(detail.getSubject());
        helper.setFrom(utilities.getParam("FROM_MAIL"));
        javaMailSender.send(message);
        log.info("-- Fin envoi mail --");
    }

    @Async
    public CompletableFuture<Void> accountCreation(User user, String password) {
        log.info("-- Debut envoi mail creation de compte --");
        boolean sendMail = Boolean.parseBoolean(utilities.getParam("SEND_MAIL"));

        if (sendMail) {
            log.info("-- --");
            try {
                this.sendMail(EmailRequest.builder()
                        .to(new String[]{user.getEmail()})
                        .login(user.getLogin())
                        .password(password)
                        .subject(utilities.getParam("ACCOUNT_CREATION_SUBJECT"))
                        .template(accountCreationTemplate)
                        .build());
            } catch (MessagingException e) {
                log.error("## Erreur lors de l'envoi du mail de la creation de compte : {}", user.getEmail());
                e.printStackTrace();
            }
        } else {
            log.info("-- --");
        }
        log.info("-- Fin envoi mail creation de compte --");
        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<Void> passwordReset(User user, String password, String resetUrl) {
        boolean sendMail = Boolean.parseBoolean(utilities.getParam("SEND_MAIL"));
        if (sendMail) {
            try {
                this.sendMail(EmailRequest.builder()
                        .to(new String[]{user.getEmail()})
//                        .login(user.getLogin())
                        .user(user)
                        .password(password)
                        .resetUrl(resetUrl)
                        .ttl(utilities.getParam("TTL").concat(" minutes"))
                        .subject(utilities.getParam("PASSWORD_RESET_SUBJECT"))
                        .template(accountResetPasswordTemplate)
                        .build());
            } catch (MessagingException e) {
                log.error("## Erreur lors de l'envoi du mail de reinitialisation de mot de passe : {}", user.getEmail());
                e.printStackTrace();
//                throw new RuntimeException(TechnicalMessage.ERROR_MESSAGE);
            }
        }
        return CompletableFuture.completedFuture(null);
    }

//    @Async
//    public CompletableFuture<Void> passwordReset(EmailRequest request) {
//        boolean sendMail = Boolean.parseBoolean(utilities.getParam("SEND_MAIL"));
//        if (sendMail) {
//            try {
//                request.setSubject(utilities.getParam("PASSWORD_RESET_SUBJECT"));
//                request.setTemplate(accountResetPasswordTemplate);
//                this.sendMail(request);
//            } catch (MessagingException e) {
//                e.printStackTrace();
//                log.error("## Erreur lors de l'envoi du mail de reinitialisation de mot de passe : {}", request.getTo());
////                throw new RuntimeException(TechnicalMessage.ERROR_MESSAGE);
//            }
//        }
//        return CompletableFuture.completedFuture(null);
//    }

    @Async
    public CompletableFuture<Void> accountUnlocked(String email) {
        boolean sendMail = Boolean.parseBoolean(utilities.getParam("SEND_MAIL"));
        if (sendMail) {
            try {
                this.sendMail(EmailRequest.builder()
                        .to(new String[]{email})
                        .subject(utilities.getParam("ACCOUNT_UNLOCK_SUBJECT"))
                        .template(accountUnlockTemplate)
                        .build());
            } catch (MessagingException e) {
                log.error("## Erreur lors de l'envoi du mail de deblocage de compte : {}", email);
                e.printStackTrace();
            }
        }
        return CompletableFuture.completedFuture(null);
    }

}
