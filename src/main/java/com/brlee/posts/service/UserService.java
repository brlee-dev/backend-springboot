package com.brlee.posts.service;

import com.brlee.posts.entity.User;
import com.brlee.posts.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public String registerUser(String username, String password) {
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
        user.setPassword(password); // You might want to hash the password here
        userRepository.save(user);

        return "User registered successfully";
    }
}
