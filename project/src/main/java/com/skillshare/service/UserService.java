package com.skillshare.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skillshare.exception.ResourceNotFoundException;
import com.skillshare.model.User;
import com.skillshare.payload.SignupRequest;
import com.skillshare.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), 
                user.getPassword(), 
                new ArrayList<>());
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    public User createUser(SignupRequest signupRequest) {
        User user = new User();
        user.setName(signupRequest.getName());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setBio(signupRequest.getBio());
        user.setProfileImage(signupRequest.getProfileImage());
        
        if (signupRequest.getInterests() != null) {
            user.setInterests(Arrays.asList(signupRequest.getInterests()));
        }
        
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(User user) {
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    public User processOAuthUser(Map<String, String> oauthUserInfo) {
        String email = oauthUserInfo.get("email");
        Optional<User> existingUser = userRepository.findByEmail(email);
        
        if (existingUser.isPresent()) {
            return existingUser.get();
        }
        
        User user = new User();
        user.setName(oauthUserInfo.get("name"));
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(java.util.UUID.randomUUID().toString()));
        user.setProvider(oauthUserInfo.getOrDefault("provider", "google"));
        user.setProfileImage(oauthUserInfo.get("picture"));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        return userRepository.save(user);
    }
}