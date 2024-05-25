package com.example.userserviceecom.controllers;

import com.example.userserviceecom.dtos.LogOutRequestDto;
import com.example.userserviceecom.dtos.LoginRequestDto;
import com.example.userserviceecom.dtos.SignUpRequestDto;
import com.example.userserviceecom.dtos.UserDto;
import com.example.userserviceecom.models.Token;
import com.example.userserviceecom.models.User;
import com.example.userserviceecom.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;
    UserController(UserService userService){
        this.userService = userService;
    }
    @PostMapping("/signup")
    public UserDto signup(@RequestBody SignUpRequestDto requestDto){
        User user= userService.signUp(
                requestDto.getEmail(),
                requestDto.getName(),
                requestDto.getPassword()
        );

        return UserDto.from(user);
    }
    @PostMapping("/login")
    public Token login(@RequestBody LoginRequestDto requestDto){
        return null;
    }
    @PostMapping("/logout")
    public ResponseEntity logout(@RequestBody LogOutRequestDto requestDto){
        return null;
    }
    @GetMapping ("/validate/{token}")
    public UserDto validateToken(@PathVariable String token){
        return null;
    }
    @GetMapping("/users/{id}")
    public UserDto getUserById(@PathVariable Long id){
        return null;
    }
}
