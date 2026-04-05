package com.example.EventSphere.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class UserLoginRequest {
    private String email;
    private String password;
}
