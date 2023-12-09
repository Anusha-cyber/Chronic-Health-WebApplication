package com.smqa.smqa.tokenauthorizer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smqa.smqa.JwtTokenService.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
@Service
public class TokenAuthorizer {
    public TokenAuthorizer(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    private JwtTokenProvider jwtTokenProvider;

    public String checkUserAndSendEmail(String authorizationHeader) {
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                // Extract the token from the Authorization header
                String jwtToken = authorizationHeader.substring(7); // Remove "Bearer " prefix
                // Validate the token and retrieve the email
                String tokenEmail = jwtTokenProvider.validateTokenAndGetSubject(jwtToken);
                Map<String, String> items = mapString(tokenEmail);
                return items.get("email");
            } else {
                throw new UnauthorizedException("Invalid or missing Bearer token");
            }
        } catch (Exception e) {
            throw new TokenExpiredException("Token has expired");
        }
    }

    public String checkAdminUser(String authorizationHeader) {
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                // Extract the token from the Authorization header
                String jwtToken = authorizationHeader.substring(7); // Remove "Bearer " prefix
                // Validate the token
                String tokenEmail = jwtTokenProvider.validateTokenAndGetSubject(jwtToken);
                Map<String, String> items = mapString(tokenEmail);
                return items.get("role");
            } else {
                throw new UnauthorizedException("Invalid or missing Bearer token");
            }
        } catch (Exception e) {
            throw new TokenExpiredException("Token has expired");
        }
    }

    private Map<String, String> mapString(String token) {
        try {
            // Create an ObjectMapper
            ObjectMapper objectMapper = new ObjectMapper();

            // Convert JSON string to Map
            return objectMapper.readValue(token, Map.class);

        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }
}


