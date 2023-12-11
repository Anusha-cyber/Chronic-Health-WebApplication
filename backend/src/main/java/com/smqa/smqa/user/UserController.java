package com.smqa.smqa.user;

import com.smqa.smqa.JwtTokenService.JwtTokenProvider;
import com.smqa.smqa.datatypes.Role;
import com.smqa.smqa.tokenauthorizer.TokenAuthorizer;
import com.smqa.smqa.requests.LoginRequest;
import com.smqa.smqa.requests.UserDetailsDto;
import com.smqa.smqa.requests.UserRequest;
import com.smqa.smqa.tokenauthorizer.TokenExpiredException;
import com.smqa.smqa.tokenauthorizer.UnauthorizedException;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;


@RestController
@RequestMapping("/api/users")
@Controller
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    private final TokenAuthorizer authorizer;
    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider, TokenAuthorizer authorizer) {
        this.userService = userService;
        this.authorizer = authorizer;
    }

    // Inject UserService through constructor


    @PostMapping("/register")
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserRequest userRequest, BindingResult bindingResult, @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader) {
        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(Collections.singletonMap("error", "Unauthorized, token required"));
            }

            if(!authorizer.checkAdminUser(authorizationHeader).equals("ADMIN")){
                return ResponseEntity.status(401).body(Collections.singletonMap("error", "Unauthorized, token required"));

            }
            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                for (FieldError fieldError : bindingResult.getFieldErrors()) {
                    errors.put("error", fieldError.getDefaultMessage());
                }

                return ResponseEntity.badRequest().body(errors);
            }
            userService.registerUser(
                    userRequest.getEmail(),
                    userRequest.getName(),
                    userRequest.getAddress(),
                    userRequest.getPhone(),
                    userRequest.getPassword(),
                    userRequest.getDateOfBirth(),
                    userRequest.getRole()
            );

            return ResponseEntity.status(201).body("User registered successfully");
        }  catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error",e.getMessage()));
        }
    }
    @GetMapping
    public ResponseEntity<Object> getAllUserDetails(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader) {
        try{
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(Collections.singletonMap("error", "Unauthorized, token required"));
            }

            if(!authorizer.checkAdminUser(authorizationHeader).equals("ADMIN")){
                return ResponseEntity.status(401).body(Collections.singletonMap("error", "Unauthorized, token required"));

            }

        List<UserDetailsDto> userDetailsList = userService.getAllUserDetails();
        return ResponseEntity.ok(userDetailsList);
        }catch (Exception e){
            return ResponseEntity.status(400).body(Collections.singletonMap("error", e.getMessage()));

        }
    }
    @GetMapping("/profile/{email}")
    public ResponseEntity<Object> getUserProfileByEmail(@PathVariable String email,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader) {
        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(Collections.singletonMap("error", "Unauthorized, token required"));
            }

            String userEmail;
            if(authorizer.checkAdminUser(authorizationHeader).equals("ADMIN")){
                userEmail=email;
            }
            else{
                userEmail = authorizer.checkUserAndSendEmail(authorizationHeader);

            }
            UserDetailsDto userDetailsDto = userService.getUserDetailsByEmail(userEmail);

            return ResponseEntity.ok(userDetailsDto);

        } catch (UnauthorizedException e) {
            return ResponseEntity.status(401).body(Collections.singletonMap("error", e.getMessage()));
        } catch (TokenExpiredException e) {
            return ResponseEntity.status(403).body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Error updating user details: " + e.getMessage()));
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<Object> updateUserDetails(
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) LocalDate dob,@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader) {

        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(Collections.singletonMap("error", "Unauthorized, token required"));
            }

            String userEmail;
            if(authorizer.checkAdminUser(authorizationHeader).equals("ADMIN")){
                userEmail=email;
            }
            else{
                userEmail = authorizer.checkUserAndSendEmail(authorizationHeader);

            }
            if (phone != null && !phone.matches("\\d{10}")) {
                return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Invalid phone number format. It should be 10 digits."));
            }
            String successMessage = userService.updateUserDetails(userEmail, address, phone, dob);
            return ResponseEntity.ok(Collections.singletonMap("message",successMessage));
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(401).body(Collections.singletonMap("error", e.getMessage()));
        } catch (TokenExpiredException e) {
            return ResponseEntity.status(403).body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Error updating user details: " + e.getMessage()));
        }
    }


    @PostMapping("/login")
    public ResponseEntity<Object> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            String jwtToken = userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());
            return ResponseEntity.status(200).body(Map.of("token", jwtToken));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error",e.getMessage()));
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<Object> changePassword(
            @RequestParam (required = false) String currentPassword,
            @RequestParam (required = false) String newPassword,
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader) {

        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(Collections.singletonMap("error", "Unauthorized, token required"));
            }
            if(currentPassword==null){
               return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Old password required" ));
            }
            if(newPassword==null){
                return ResponseEntity.badRequest().body(Collections.singletonMap("error", "New password is missing" ));
            }
            String email = authorizer.checkUserAndSendEmail(authorizationHeader);

            userService.changePassword(email, currentPassword, newPassword);

            return ResponseEntity.status(201).body(Collections.singletonMap("message", "Password changed successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Error changing password: " + e.getMessage()));
        }
    }
}
