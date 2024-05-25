package com.example.userserviceecom.services;

import com.example.userserviceecom.exceptions.UserNotFoundException;
import com.example.userserviceecom.models.Token;
import com.example.userserviceecom.models.User;
import com.example.userserviceecom.repositories.TokenRepository;
import com.example.userserviceecom.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserRepository userRepository;
    private TokenRepository tokenRepository;

    UserService(BCryptPasswordEncoder bCryptPasswordEncoder,
                UserRepository userRepository,
                TokenRepository tokenRepository){
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
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
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if(optionalUser.isEmpty()){
            throw new UserNotFoundException("user with email " + email + "doesn't exist.");
        }

        User user = optionalUser.get();

        if(!bCryptPasswordEncoder.matches(password, user.getHashedPassword())){
            //Throw some exception
            return null;
        }

        //login successful, generate a token
        Token token = generateToken(user);
        Token savedToken= tokenRepository.save(token);
        return savedToken;
    }

    private Token generateToken(User user){
        LocalDate currentDate = LocalDate.now();
        LocalDate thirtyDaysLater = currentDate.plusDays(30);

        Date expiryDate = Date.from(thirtyDaysLater.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Token token = new Token();
        token.setExpiryAt(expiryDate);
        //128 alpha numeric string
        token.setValue(RandomStringUtils.randomAlphanumeric(128));
        token.setUser(user);

        return token;


    }
    public void logout(String tokenValue){
            Optional<Token> optionalToken= tokenRepository.findByValueAndDeleted(tokenValue, false);

            if (optionalToken.isEmpty()){
                //Throw new exception
                return;
            }

            Token token= optionalToken.get();
            token.setDeleted(true);
            tokenRepository.save(token);


    }

    public User validateToken(String token){
        Optional<Token> optionalToken= tokenRepository.findByValueAndDeletedAndExpiryAtGreaterThan(token, false, new Date());

        if(optionalToken.isEmpty()){
            //throw new exception
            return null;
        }

        return optionalToken.get().getUser();
    }
}
