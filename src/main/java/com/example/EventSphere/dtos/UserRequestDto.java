package com.example.EventSphere.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
public class UserRequestDto {
    private String name;
    private String email;
    private String phone;
    private String password;
}
