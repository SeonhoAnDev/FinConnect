package com.example.finconnect.controller;

import com.example.finconnect.model.user.User;
import com.example.finconnect.model.user.UserAuthenticationResponse;
import com.example.finconnect.model.user.UserLoginRequestBody;
import com.example.finconnect.model.user.UserSignUpRequestBody;
import com.example.finconnect.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping
    public ResponseEntity<User> signUp(
            @Valid @RequestBody
            UserSignUpRequestBody userSignUpRequestBody) {
        var user =
                userService.signUp(userSignUpRequestBody.username(), userSignUpRequestBody.password());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<UserAuthenticationResponse> authenticate(
            @Valid @RequestBody
            UserLoginRequestBody userLoginRequestBody) {
        var response =
                userService.authenticate(userLoginRequestBody.username(), userLoginRequestBody.password());
        return ResponseEntity.ok(response);
    }
}
