package com.example.EventSphere.service;

import com.example.EventSphere.enums.PaymentStatus;
import org.springframework.stereotype.Service;

import java.util.Random;
@Service
public class PaymentService{
    public PaymentStatus processPayment(double amount) throws InterruptedException {
        Thread.sleep(2000);
        int randomNumber = new Random().nextInt(100);
        if(randomNumber < 80){
            return PaymentStatus.SUCCESS;
        }
            return PaymentStatus.FAILED;

    }
}
