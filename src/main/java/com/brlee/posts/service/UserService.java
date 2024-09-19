package com.brlee.posts.service;

import com.brlee.posts.entity.User;
import com.brlee.posts.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // BCryptPasswordEncoder 인스턴스 생성
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String registerUser(String username, String password) {
        // 유효성 검사
        if (username.length() < 4 || username.length() > 10 || !username.matches("[a-z0-9]+")) {
            return "Invalid username";
        }
        if (password.length() < 8 || password.length() > 15 || !password.matches("[a-zA-Z0-9]+")) {
            return "Invalid password";
        }

        // 중복 사용자 체크
        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            return "Username already exists";
        }

        // 새 사용자 생성 및 비밀번호 암호화
        User user = new User();
        user.setUsername(username);
        String encodedPassword = passwordEncoder.encode(password); // 비밀번호 암호화
        user.setPassword(encodedPassword); // 암호화된 비밀번호 저장
        userRepository.save(user);

        return "User registered successfully";
    }
}
