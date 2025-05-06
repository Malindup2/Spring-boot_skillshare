package com.skillshare.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillshare.exception.ResourceNotFoundException;
import com.skillshare.exception.UnauthorizedException;
import com.skillshare.model.User;
import com.skillshare.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody User userUpdate,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        User currentUser = userService.findByEmail(userDetails.getUsername());
        
        // Only allow users to update their own profile
        if (!currentUser.getId().equals(id)) {
            throw new UnauthorizedException("You are not authorized to update this user");
        }
        
        User existingUser = userService.getUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        
        // Update only allowed fields
        existingUser.setName(userUpdate.getName());
        existingUser.setBio(userUpdate.getBio());
        existingUser.setProfileImage(userUpdate.getProfileImage());
        existingUser.setInterests(userUpdate.getInterests());
        
        User updatedUser = userService.updateUser(existingUser);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = userService.findByEmail(userDetails.getUsername());
        
        // Only allow users to delete their own account
        if (!currentUser.getId().equals(id)) {
            throw new UnauthorizedException("You are not authorized to delete this user");
        }
        
        userService.getUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        
        userService.deleteUser(id);
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "User deleted successfully");
        return ResponseEntity.ok(response);
    }
}