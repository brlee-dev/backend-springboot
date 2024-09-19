package com.brlee.posts.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CSRF 비활성화 및 인증 없이 모든 요청 허용
        http.csrf().disable()
            .authorizeRequests()
            .anyRequest().permitAll();  // 모든 요청을 허용
        return http.build();
    }
}
