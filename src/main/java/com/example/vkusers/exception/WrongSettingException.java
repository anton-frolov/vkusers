package com.example.vkusers.exception;

public class WrongSettingException extends RuntimeException{
    public WrongSettingException(String message) {
        super(message);
    }
}
