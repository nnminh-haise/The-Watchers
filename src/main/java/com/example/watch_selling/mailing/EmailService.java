package com.example.watch_selling.mailing;

import com.example.watch_selling.dtos.ResponseDto;

public interface EmailService {
    ResponseDto<Boolean> sendEmail(String recipient, String subject, String body);
}
