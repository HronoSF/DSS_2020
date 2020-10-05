package com.hronosf.crawler.services.impl;

import com.google.common.base.Joiner;
import com.hronosf.crawler.services.EmailSenderService;
import com.hronosf.crawler.util.CrawlerStateStorage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailSenderServiceImpl implements EmailSenderService {

    @Value(("${mail.admin.address}"))
    private String to;

    @Value("${mail.subject}")
    private String subject;

    private String letterHtml;
    private final JavaMailSender javaMailSenderClient;

    @PostConstruct
    @SneakyThrows
    public void loadTemplate() {
        letterHtml = IOUtils
                .toString(this.getClass().getResourceAsStream("templates/letter.html"), Charset.defaultCharset());
    }

    @SneakyThrows
    public void sendMessageAboutCrawlerFailureToAdmin() {
        Map<String, String> crawlerErrorsToNotify = CrawlerStateStorage.getCrawlerErrorsToNotify();

        if (!crawlerErrorsToNotify.isEmpty()) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setFrom("Vkontakte Crawler");
            message.setSubject(subject);
            message.setText(String.format(letterHtml,
                    Joiner.on("\n").withKeyValueSeparator(" - ").join(crawlerErrorsToNotify)));

            // send email:
            javaMailSenderClient.send(message);

            // clear errors because admin were informed:
            crawlerErrorsToNotify.clear();
        }
    }
}
