package com.brlee.posts.service;

import com.brlee.posts.entity.User;
import com.brlee.posts.repository.UserRepository;
import com.brlee.posts.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String registerUser(String username, String password) {
        try {
            if (username.length() < 4 || username.length() > 10 || !username.matches("[a-z0-9]+")) {
                return "Invalid username";
            }
            if (password.length() < 8 || password.length() > 15 || !password.matches("[a-zA-Z0-9]+")) {
                return "Invalid password";
            }

            Optional<User> existingUser = userRepository.findByUsername(username);
            if (existingUser.isPresent()) {
                return "Username already exists";
            }

            User user = new User();
            user.setUsername(username);
            String encodedPassword = passwordEncoder.encode(password);
            user.setPassword(encodedPassword);
            userRepository.save(user);

            return "User registered successfully";
        } catch (Exception e) {
            // 예외가 발생하면 에러 메시지 반환
            return "Registration failed due to an internal error: " + e.getMessage();
        }
    }

    public String login(String username, String password) {
        try {
            Optional<User> existingUser = userRepository.findByUsername(username);
            if (!existingUser.isPresent()) {
                return "User not found";
            }

            User user = existingUser.get();
            if (!passwordEncoder.matches(password, user.getPassword())) {
                return "Invalid password";
            }

            // 로그인 성공 시 JWT 토큰 발급
            return jwtUtil.generateToken(username);
        } catch (Exception e) {
            // 예외가 발생하면 에러 메시지 반환
            return "Login failed due to an internal error: " + e.getMessage();
        }
    }
}
