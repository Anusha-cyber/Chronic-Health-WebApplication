package com.smqa.smqa.JwtTokenService;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtTokenProvider {

    private final Algorithm jwtAlgorithm;

    public JwtTokenProvider(Algorithm jwtAlgorithm) {
        this.jwtAlgorithm = jwtAlgorithm;
    }

    public String generateToken(String email, Enum role) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("email",email);
        responseMap.put("role",role);
        Date expirationTime = new Date(System.currentTimeMillis() + 3600000); // 1 hour in milliseconds

        return JWT.create()
                .withSubject(mapToJsonString(responseMap))
                .withExpiresAt(expirationTime)
                .sign(jwtAlgorithm);
    }

    public String validateTokenAndGetSubject(String token) {
        try {
            DecodedJWT decodedJWT = JWT.require(jwtAlgorithm).build().verify(token);
            return decodedJWT.getSubject();
        } catch (JWTVerificationException e) {
            // Handle the exception, e.g., log it or perform specific actions
            System.out.println("Token verification failed: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
    private static String mapToJsonString(Map<String, Object> map) {
        try {
            // Using Jackson ObjectMapper to convert Map to JSON string
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(map);
        } catch (Exception e) {
            e.printStackTrace();
            return "{}"; // Return an empty JSON object in case of an exception
        }

}
}

