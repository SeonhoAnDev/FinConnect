package com.example.finconnect.exception.user;

import com.example.finconnect.exception.ClientErrorException;
import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends ClientErrorException {
    public UserAlreadyExistsException() {
        super(HttpStatus.CONFLICT,"User already exists.");
    }

    public UserAlreadyExistsException(Long username) {
        super(HttpStatus.CONFLICT,"User with username " + username + " already exists");
    }

}
