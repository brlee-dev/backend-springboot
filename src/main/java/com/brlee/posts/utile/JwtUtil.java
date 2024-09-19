package com.brlee.posts.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    // 32바이트 이상의 Base64로 인코딩된 SECRET_KEY
    private final String SECRET_KEY = "RtCicvlkcYfXQZSaWxgZx6ojNxfnvB9u4TxSIh0eMnE=";  // 예시 키. 실제 운영 시 안전한 키 사용 권장.
    private final long JWT_EXPIRATION = 36000000;  // 10시간 (밀리초)

    // Base64로 인코딩된 SECRET_KEY를 SecretKey로 변환하는 메서드
    private SecretKey getSigningKey() {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
            if (keyBytes.length < 32) {
                throw new IllegalArgumentException("The SECRET_KEY must be at least 256 bits (32 bytes)");
            }
            return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
        } catch (IllegalArgumentException e) {
            System.err.println("Error decoding SECRET_KEY: " + e.getMessage());
            throw new RuntimeException("Invalid SECRET_KEY", e);
        }
    }

    // JWT 토큰 생성 메서드
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    // 토큰 생성 메서드
    private String createToken(Map<String, Object> claims, String subject) {
        try {
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(subject)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))  // 토큰 만료 시간
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)  // 서명 및 알고리즘
                    .compact();
        } catch (Exception e) {
            System.err.println("Error generating JWT: " + e.getMessage());
            e.printStackTrace();  // 상세 스택 트레이스 출력
            throw new RuntimeException("Error generating JWT", e);
        }
    }
}
