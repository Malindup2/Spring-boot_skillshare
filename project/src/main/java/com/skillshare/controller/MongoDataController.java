package com.skillshare.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/mongo")
public class MongoDataController {

    @Autowired
    private MongoTemplate mongoTemplate;

    @GetMapping("/collections")
    public ResponseEntity<List<String>> getCollections() {
        return ResponseEntity.ok(mongoTemplate.getCollectionNames().stream().toList());
    }

    @GetMapping("/data/{collectionName}")
    public ResponseEntity<?> getCollectionData(@PathVariable String collectionName) {
        List<?> data = mongoTemplate.findAll(Object.class, collectionName);
        return ResponseEntity.ok(data);
    }
}