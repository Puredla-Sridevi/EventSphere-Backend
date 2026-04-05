package com.example.EventSphere.controller;

import com.example.EventSphere.dtos.UserLoginRequest;
import com.example.EventSphere.dtos.UserRequestDto;
import com.example.EventSphere.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;
    @PostMapping("register")
    public ResponseEntity<String> registerUser(@RequestBody UserRequestDto userRequestDto){
return ResponseEntity.ok(userService.registerUser(userRequestDto));
    }
    @PostMapping("login")
    public ResponseEntity<String> loginUser(@RequestBody UserLoginRequest loginRequest){
return ResponseEntity.ok(userService.loginUser(loginRequest));
    }
}
