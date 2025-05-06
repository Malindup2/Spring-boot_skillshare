package com.skillshare.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class MongoDBHealthController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/db")
    public ResponseEntity<?> checkDBStatus() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Execute a simple command to check if MongoDB is responsive
            Document result = mongoTemplate.getDb().runCommand(new Document("ping", 1));
            boolean isSuccess = result.getDouble("ok") == 1.0;
            
            if (isSuccess) {
                response.put("status", "UP");
                response.put("database", mongoTemplate.getDb().getName());
                response.put("message", "MongoDB connection is successful");
            } else {
                response.put("status", "DOWN");
                response.put("message", "MongoDB ping command failed");
                response.put("result", result.toJson());
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "DOWN");
            response.put("message", "MongoDB connection failed");
            response.put("error", e.getMessage());
            
            return ResponseEntity.status(503).body(response);
        }
    }
}