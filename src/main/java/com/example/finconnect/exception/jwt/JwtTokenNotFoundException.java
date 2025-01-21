package com.example.finconnect.exception.jwt;

import io.jsonwebtoken.JwtException;

public class JwtTokenNotFoundException extends JwtException {
    public JwtTokenNotFoundException() {
        super("JWT token not found");
    }
}
