package com.skillshare.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skillshare.model.Resource;
import com.skillshare.model.ResourceType;
import com.skillshare.service.ResourceService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/resources")
@RequiredArgsConstructor
public class ResourceController {
    private final ResourceService resourceService;

    @GetMapping
    public ResponseEntity<Page<Resource>> getAllResources(Pageable pageable) {
        return ResponseEntity.ok(resourceService.getAllResources(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> getResource(@PathVariable String id) {
        return ResponseEntity.ok(resourceService.getResourceById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Resource>> searchResources(
            @RequestParam String keyword,
            Pageable pageable) {
        return ResponseEntity.ok(resourceService.searchResources(keyword, pageable));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Page<Resource>> getResourcesByCategory(
            @PathVariable String category,
            Pageable pageable) {
        return ResponseEntity.ok(resourceService.getResourcesByCategory(category, pageable));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<Page<Resource>> getResourcesByType(
            @PathVariable ResourceType type,
            Pageable pageable) {
        return ResponseEntity.ok(resourceService.getResourcesByType(type, pageable));
    }

    @PostMapping
    public ResponseEntity<Resource> createResource(
            @Valid @RequestBody Resource resource,
            Authentication authentication) {
        // TODO: Set the user from authentication when that functionality is implemented
        return ResponseEntity.ok(resourceService.createResource(resource));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Resource> updateResource(
            @PathVariable String id,
            @Valid @RequestBody Resource resource) {
        return ResponseEntity.ok(resourceService.updateResource(id, resource));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResource(@PathVariable String id) {
        resourceService.deleteResource(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<Resource>> getUserResources(
            @PathVariable String userId,
            Pageable pageable) {
        return ResponseEntity.ok(resourceService.getUserResources(userId, pageable));
    }
}