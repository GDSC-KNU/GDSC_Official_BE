package com.gdsc_knu.official_homepage.service;

import com.gdsc_knu.official_homepage.entity.application.Application;
import com.gdsc_knu.official_homepage.entity.enumeration.ApplicationStatus;
import com.gdsc_knu.official_homepage.exception.CustomException;
import com.gdsc_knu.official_homepage.exception.ErrorCode;
import com.gdsc_knu.official_homepage.repository.ApplicationRepository;
import com.gdsc_knu.official_homepage.repository.RedisCustomRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

import org.thymeleaf.context.Context;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final ApplicationRepository applicationRepository;
    private final RedisCustomRepository redisRepository;

    @Value("${spring.mail.username}")
    private String sender;

    private static final String MAIL_TITLE = "GDSC KNU 지원 결과 안내";
    private static final String PASS_TEMPLATE = "pass";
    private static final String FAIL_TEMPLATE = "fail";
    private static final String REDIS_KEY = "FAILED_MAIL_SET";

    public void send() {
        List<Application> applications = applicationRepository.findByApplicationStatusIn(
                Arrays.asList(ApplicationStatus.APPROVED, ApplicationStatus.REJECTED)
        );
        for (Application application : applications) {
            String mailTemplate = createTemplate(application);
            sendMail(application.getEmail(), mailTemplate);
        }
    }

    public void sendOne(Application application) {
        String mailTemplate = createTemplate(application);
        sendMail(application.getEmail(), mailTemplate);
        redisRepository.deleteData(REDIS_KEY, application.getEmail());
    }

    /**
     * 메일 전송
     * 전송 실패시 (MessagingException, MailException) redis에 실패한 이메일 저장 후 예외를 발생시킨다.
     */
    private void sendMail(String email, String mailTemplate) {
        try {
//            throw new MessagingException("OMG");
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "utf-8");

            messageHelper.setFrom(sender);
            messageHelper.setTo(email);
            messageHelper.setSubject(MAIL_TITLE);
            messageHelper.setText(mailTemplate, true);

            mailSender.send(message);
        } catch (MessagingException | MailException e) {
            redisRepository.addData(REDIS_KEY, email);
            throw new CustomException(ErrorCode.FAILED_SEND_MAIL, email + "의 메일 전송에 실패하였습니다: " + e.getMessage());
        }
    }

    private String createTemplate(Application application) {
        Context context = new Context();
        context.setVariable("name", application.getName());
        String templateName = application.getApplicationStatus() == ApplicationStatus.APPROVED ? PASS_TEMPLATE : FAIL_TEMPLATE;
        return templateEngine.process(templateName, context);
    }

}
