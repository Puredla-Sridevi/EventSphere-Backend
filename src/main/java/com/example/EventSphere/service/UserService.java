package com.example.EventSphere.service;

import com.example.EventSphere.enums.Role;
import com.example.EventSphere.config.JwtService;
import com.example.EventSphere.dtos.UserLoginRequest;
import com.example.EventSphere.dtos.UserRequestDto;
import com.example.EventSphere.entity.Users;
import com.example.EventSphere.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final RateLimiterService limiterService;
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

//        if( ! existsByEmail(loginRequest.getEmail())){
//           throw new UsernameNotFoundException("UserNotFound");
//        }
//        String key="loginlimituser_"+loginRequest.getEmail();
//        if(!limiterService.isAllowed(key)){
//            throw new IllegalArgumentException("You are Done IWth Your Login Attempts");
//        }
//        Users users=getUserByEmail(loginRequest.getEmail());
//        if( ! encoder.matches(loginRequest.getPassword(),users.getPassword())){
//            throw new AccessDeniedException("Invalid Password");
//        }
//       return jwtService.generateToken(users);
        String key="loginlimituser_"+loginRequest.getEmail();
        if(!limiterService.isLoginAllowed(key)){
            throw new IllegalArgumentException("Too many login attempts. Try again later.");
        }
        Users users=userRepository.findByEmail(loginRequest.getEmail()).orElse(null);
        if( users==null || ! encoder.matches(loginRequest.getPassword(),users.getPassword())){
            limiterService.recordFailure(key);
            throw new BadCredentialsException("username Or Password is not correct");
        }
        limiterService.reset(key);
        return jwtService.generateToken(users);
    }
}
