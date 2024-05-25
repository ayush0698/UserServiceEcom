package com.example.userserviceecom.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String mesage){
        super(mesage);
    }
}
