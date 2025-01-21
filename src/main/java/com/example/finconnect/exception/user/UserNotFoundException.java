package com.example.finconnect.exception.user;

import com.example.finconnect.exception.ClientErrorException;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ClientErrorException {
    public UserNotFoundException() {
        super(HttpStatus.NOT_FOUND,"User not found");
    }

    public UserNotFoundException(Long username) {
        super(HttpStatus.NOT_FOUND,"User with username " + username + " not found");
    }

}
