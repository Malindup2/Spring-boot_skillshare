package com.skillshare.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.bson.Document;

@Component
public class StartupHealthCheck {
    
    private static final Logger logger = LoggerFactory.getLogger(StartupHealthCheck.class);
    
    @Autowired
    private MongoTemplate mongoTemplate;
    
    @EventListener(ApplicationReadyEvent.class)
    public void checkDatabaseConnection() {
        logger.info("Performing MongoDB connection health check on startup...");
        
        try {
            Document result = mongoTemplate.getDb().runCommand(new Document("ping", 1));
            boolean isSuccess = result.getDouble("ok") == 1.0;
            
            if (isSuccess) {
                logger.info("✓ MongoDB connection successful! Connected to database: {}", 
                        mongoTemplate.getDb().getName());
            } else {
                logger.error("✗ MongoDB ping command failed: {}", result.toJson());
            }
        } catch (Exception e) {
            logger.error("✗ MongoDB connection failed on startup!", e);
            logger.error("Error details: {}", e.getMessage());
            
            // We're not failing the application startup, just logging the error
            // If you want to fail fast when MongoDB is not available, uncomment:
            // throw new RuntimeException("Application failed to start due to MongoDB connection issue", e);
        }
    }
}