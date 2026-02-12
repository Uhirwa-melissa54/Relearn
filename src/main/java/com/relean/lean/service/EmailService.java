package com.relean.lean.service;

import com.relean.lean.dtos.EmailDto;
import com.relean.lean.dtos.RegisterResponse;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service

public class EmailService {

    @Autowired private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}") private String sender;


    public String sendSimpleMail(EmailDto details)
    {


        try {


            SimpleMailMessage mailMessage
                    = new SimpleMailMessage();

            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getEmail());
            mailMessage.setText(details.getMsgBody());



            javaMailSender.send(mailMessage);
            return "Mail Sent Successfully...";
        }


        catch (Exception e) {
            return "Error while Sending Mail";
        }
    }

}