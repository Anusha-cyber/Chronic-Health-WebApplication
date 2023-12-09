package com.smqa.smqa.JwtTokenService;

import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Bean
    public Algorithm jwtAlgorithm() {

        String secretKey = "secretString";
        return Algorithm.HMAC256(secretKey);
    }
}
