package com.skillshare.controller;

import java.util.HashMap;
import java.util.Map;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
public class MongoHealthController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/mongodb")
    public ResponseEntity<Map<String, Object>> checkMongoDBConnection() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Try to ping the database to check connection
            Document pingCommand = new Document("ping", 1);
            Document result = mongoTemplate.getDb().runCommand(pingCommand);
            
            Double okValue = result.getDouble("ok");
            if (okValue == null || okValue != 1.0) {
                throw new Exception("Ping command did not return ok=1.0");
            }
            
            // If it reaches here, connection is successful
            response.put("status", "UP");
            response.put("message", "MongoDB connection is successful");
            response.put("databaseName", mongoTemplate.getDb().getName());
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // If there's an exception, connection failed
            response.put("status", "DOWN");
            response.put("message", "MongoDB connection failed");
            response.put("error", e.getMessage());
            response.put("timestamp", System.currentTimeMillis());
            
            return ResponseEntity.status(503).body(response);
        }
    }
}