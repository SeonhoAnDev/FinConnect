package com.example.finconnect.exception.user;

import com.example.finconnect.exception.ClientErrorException;
import org.springframework.http.HttpStatus;

public class UserNotAllowedException extends ClientErrorException {
    public UserNotAllowedException() {
        super(HttpStatus.FORBIDDEN,"User not allowed");
    }
}
