package com.hronosf.crawler.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.io.IOException;
import java.util.Properties;

@SpringBootConfiguration
public class EmailConfiguration {

    @Bean
    public JavaMailSender mailSender(@Value("${mail.host}") String host,
                                     @Value("${mail.port}") String port,
                                     @Value("${mail.username}") String username,
                                     @Value("${mail.password}") String password,
                                     @Value("${mail.protocol}") String protocol,
                                     @Autowired Properties javaMailProperties) {

        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(host);
        javaMailSender.setPort(Integer.parseInt(port));
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);
        javaMailSender.setProtocol(protocol);
        javaMailSender.setJavaMailProperties(javaMailProperties);

        return javaMailSender;
    }

    @Bean
    public Properties javaMailProperties() throws IOException {
        Properties mailProperties = new Properties();

        //load a properties file from class path, inside static method
        mailProperties.load(this.getClass().getClassLoader().getResourceAsStream("properties/mail.properties"));

        return mailProperties;
    }
}
