package com.brlee.posts.controller;

import com.brlee.posts.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestParam String username, @RequestParam String password) {
        String result = userService.registerUser(username, password);
        if (result.equals("User registered successfully")) {
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } else if (result.startsWith("Registration failed")) {
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestParam String username, @RequestParam String password) {
        String token = userService.login(username, password);

        if (token.equals("User not found") || token.equals("Invalid password")) {
            return new ResponseEntity<>(token, HttpStatus.UNAUTHORIZED);
        } else if (token.startsWith("Login failed")) {
            return new ResponseEntity<>(token, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // JWT 토큰을 응답 헤더에 추가
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        return ResponseEntity.ok()
                .headers(headers)
                .body("Login successful");
    }
}
