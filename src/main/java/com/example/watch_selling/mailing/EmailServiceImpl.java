package com.example.watch_selling.mailing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.watch_selling.dtos.ResponseDto;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sendingEmail;

    public String getSendingEmail() {
        return this.sendingEmail;
    }

    @SuppressWarnings("null")
    @Override
    public ResponseDto<Boolean> sendEmail(String recipient, String subject, String body) {
        ResponseDto<Boolean> res = new ResponseDto<>(false, "Failed", HttpStatus.BAD_REQUEST);
        if (recipient == null || recipient.isEmpty() || recipient.isBlank()) {
            return res.setMessage("Recipient cannot be empty!");
        }

        if (subject == null || subject.isEmpty() || subject.isBlank()) {
            return res.setMessage("Subject cannot be empty!");
        }

        if (body == null || body.isEmpty() || body.isBlank()) {
            return res.setMessage("Body cannot be empty!");
        }

        if (this.sendingEmail == null || this.sendingEmail.isEmpty() || this.sendingEmail.isBlank()) {
            return res.setMessage("Sending email cannot be empty!");
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // Set true for multipart messages
            helper.setFrom(this.sendingEmail);
            helper.setTo(recipient);
            helper.setSubject(subject);
            helper.setText(body, true);
            mailSender.send(message);

            return res
                    .setData(true)
                    .setStatus(HttpStatus.OK)
                    .setMessage("Success!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return res
                    .setMessage(e.getMessage())
                    .setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
