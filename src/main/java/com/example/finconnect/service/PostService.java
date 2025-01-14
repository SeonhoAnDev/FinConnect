package com.example.finconnect.service;

import com.example.finconnect.model.Post;
import com.example.finconnect.model.PostPostRequestBody;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private static final List<Post> posts = new ArrayList<>();

    static {
        posts.add(new Post(1L, "Post1", ZonedDateTime.now()));
        posts.add(new Post(1L, "Post1", ZonedDateTime.now()));
        posts.add(new Post(1L, "Post1", ZonedDateTime.now()));
    }

    public List<Post> getPosts() {
        return posts;
    }

    public Optional<Post> getPostId(Long postId) {
        return posts.stream().filter(post -> postId.equals(post.postId())).findFirst();
    }

    public Post creatPost(PostPostRequestBody postpostRequestBody) {
        Long newPostId = posts.stream().mapToLong(Post::postId).max().orElse(0L) + 1;

        Post newPost = new Post(newPostId, postpostRequestBody.body(), ZonedDateTime.now());
        posts.add(newPost);

        return newPost;
    }
}
