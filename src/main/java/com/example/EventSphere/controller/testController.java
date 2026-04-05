package com.example.EventSphere.controller;

import com.example.EventSphere.dtos.MailDto;
import com.example.EventSphere.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class testController {
    private final EmailService emailService;
    @PostMapping("test/email")
    public String test(@RequestBody MailDto mailDto){
        emailService.sendBookingConfirmation(mailDto);
        return "Email Sent Successfully";
    }
}
