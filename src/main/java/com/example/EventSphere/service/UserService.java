package com.example.EventSphere.service;

import com.example.EventSphere.enums.Role;
import com.example.EventSphere.config.JwtService;
import com.example.EventSphere.dtos.UserLoginRequest;
import com.example.EventSphere.dtos.UserRequestDto;
import com.example.EventSphere.entity.Users;
import com.example.EventSphere.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    public String registerUser(UserRequestDto requestDto){
        if( existsByEmail(requestDto.getEmail())){
           throw new RuntimeException("User Already Exists");
        }
        Users users=Users.builder()
                .name(requestDto.getName())
                .role(Role.ROLE_USER)
                .email(requestDto.getEmail())
                .password(encoder.encode(requestDto.getPassword()))
                .phone(requestDto.getPhone())
                .build();
        userRepository.save(users);
        return "User Created succesfully";
    }
    public Users getUserByEmail(String email){
        if( ! existsByEmail(email)) {
            throw new UsernameNotFoundException("User Not Found");
        }
        return userRepository.findByEmail(email).get();
    }
    public boolean existsByEmail(String email){

        return userRepository.existsByEmail(email);
    }
    public String loginUser(UserLoginRequest loginRequest){
        if( ! existsByEmail(loginRequest.getEmail())){
           throw new UsernameNotFoundException("UserNotFound");
        }
        Users users=getUserByEmail(loginRequest.getEmail());
        if( ! encoder.matches(loginRequest.getPassword(),users.getPassword())){
            throw new AccessDeniedException("Invalid Password");
        }
       return jwtService.generateToken(users);
    }
}
