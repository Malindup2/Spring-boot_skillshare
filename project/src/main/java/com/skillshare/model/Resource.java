package com.skillshare.model;

import java.time.LocalDateTime;

import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Document representing a learning resource in the MongoDB database
 */
@Data
@Document(collection = "resources")
public class Resource {
    
    @Id
    private String id;
    
    @NotBlank(message = "Title is required")
    private String title;
    
    @NotBlank(message = "Description is required")
    private String description;
    
    @NotBlank(message = "URL is required")
    @URL(message = "URL must be valid")
    private String url;
    
    @NotNull(message = "Resource type is required")
    private ResourceType resourceType;
    
    @NotBlank(message = "Skill category is required")
    private String skillCategory;
    
    private Integer likes = 0;
    
    @DBRef
    private User user;
    
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