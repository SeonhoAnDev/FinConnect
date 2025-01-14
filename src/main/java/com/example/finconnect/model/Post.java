package com.example.finconnect.model;

import java.time.ZonedDateTime;
import java.util.Objects;

public record Post(Long postId, String body, ZonedDateTime createdAt){

}
