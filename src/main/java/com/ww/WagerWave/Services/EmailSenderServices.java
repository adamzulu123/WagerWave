package com.ww.WagerWave.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderServices {

    @Autowired
    private JavaMailSender mailSender;

    //wysyłanie maila z tymczasowym hasłem
    public void sendEmail(String user, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
        System.out.println("Mail sent");
    }

}
