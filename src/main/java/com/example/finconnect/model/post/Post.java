package com.example.finconnect.model.post;

import com.example.finconnect.model.entity.PostEntity;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.ZonedDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Post(
         Long postId,
         String body,
         ZonedDateTime createdDateTime,
         ZonedDateTime updateDateTime,
         ZonedDateTime deletedDataTime){
    public static Post from(PostEntity postEntity) {
        return new Post(
                postEntity.getPostId(),
                postEntity.getBody(),
                postEntity.getCreatedDateTime(),
                postEntity.getUpdatedDateTime(),
                postEntity.getDeletedDateTime());
    }
}