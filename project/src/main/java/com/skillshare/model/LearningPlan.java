package com.skillshare.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Document(collection = "learning_plans")
public class LearningPlan {
    @Id
    private String id;

    private String title;
    private String description;

    @DBRef
    private User user;

    private Set<Milestone> milestones = new HashSet<>();

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

@Data
@Document(collection = "milestones")
class Milestone {
    @Id
    private String id;

    private String title;
    private String description;

    @DBRef
    private LearningPlan learningPlan;

    private LocalDateTime targetDate;
    private boolean completed;

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