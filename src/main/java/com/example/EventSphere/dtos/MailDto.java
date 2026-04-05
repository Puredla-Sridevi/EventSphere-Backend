package com.example.EventSphere.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class MailDto {
   private  String toEmail;
   private  String eventName;
   private  int seats;
 private    double price;
}
