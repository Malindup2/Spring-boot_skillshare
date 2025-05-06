package com.skillshare.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillshare.exception.BadRequestException;
import com.skillshare.model.User;
import com.skillshare.payload.AuthRequest;
import com.skillshare.payload.AuthResponse;
import com.skillshare.payload.SignupRequest;
import com.skillshare.security.JwtTokenUtil;
import com.skillshare.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new BadRequestException("Invalid email or password");
        }

        final UserDetails userDetails = userService.loadUserByUsername(authRequest.getEmail());
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        User user = userService.findByEmail(authRequest.getEmail());

        return ResponseEntity.ok(new AuthResponse(jwt, user));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        if (userService.existsByEmail(signupRequest.getEmail())) {
            throw new BadRequestException("Email is already in use");
        }

        User user = userService.createUser(signupRequest);
        UserDetails userDetails = userService.loadUserByUsername(user.getEmail());
        String jwt = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(jwt, user));
    }
    
    @PostMapping("/oauth-login")
    public ResponseEntity<?> oauthLogin(@RequestBody Map<String, String> oauthUserInfo) {
        try {
            User user = userService.processOAuthUser(oauthUserInfo);
            UserDetails userDetails = userService.loadUserByUsername(user.getEmail());
            String jwt = jwtTokenUtil.generateToken(userDetails);
            
            return ResponseEntity.ok(new AuthResponse(jwt, user));
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "OAuth authentication failed");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}