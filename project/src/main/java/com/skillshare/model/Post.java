package com.skillshare.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Document(collection = "posts")
public class Post {
    @Id
    private String id;

    private String title;
    private String content;
    private Set<String> images = new HashSet<>();
    private String videoUrl;

    @DBRef
    private User user;

    @DBRef
    private Set<Comment> comments = new HashSet<>();

    @DBRef
    private Set<User> likes = new HashSet<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}