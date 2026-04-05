package com.example.EventSphere.service;

import com.example.EventSphere.dtos.MailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    @Async
    public void sendBookingConfirmation(MailDto mailDto){
        SimpleMailMessage mailMessage=new SimpleMailMessage();
        mailMessage.setTo(mailDto.getToEmail());
        mailMessage.setSubject("Booking Confirmed");
        mailMessage.setText(
                "your booking is confirmed!\n\n"+
                        "Event: "+mailDto.getEventName()+"\n"+
                        "Seats: "+mailDto.getSeats()+"\n"+
                        "Total Price : "+mailDto.getPrice()+"\n\n"+
                        "Thank You For Using EventSphere! "
        );
        mailSender.send(mailMessage);
    }
}
