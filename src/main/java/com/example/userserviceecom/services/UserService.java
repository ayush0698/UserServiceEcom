package com.example.userserviceecom.services;

import com.example.userserviceecom.models.Token;
import com.example.userserviceecom.models.User;
import com.example.userserviceecom.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserRepository userRepository;

    UserService(BCryptPasswordEncoder bCryptPasswordEncoder,
                UserRepository userRepository){
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
    }
    public User signUp(String email,
                       String name,
                       String password){

        User user= new User();
        user.setEmail(email);
        user.setName(name);
        user.setHashedPassword(bCryptPasswordEncoder.encode(password));
        user.setEmailVerified(true);

        //save user obj to the DB
        User savedUser = userRepository.save(user);
        return savedUser;
    }

    public Token login(String email, String password){
        return null;
    }

    public void logout(String token){

    }

    public User validateToken(String token){
        return null;
    }
}
