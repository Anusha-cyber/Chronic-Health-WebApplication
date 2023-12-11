package com.smqa.smqa.healthRecords;

import com.smqa.smqa.datatypes.Role;
import com.smqa.smqa.requests.HealthRecordUpdateRequest;
import com.smqa.smqa.tokenauthorizer.TokenAuthorizer;
import com.smqa.smqa.tokenauthorizer.TokenExpiredException;
import com.smqa.smqa.tokenauthorizer.UnauthorizedException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Controller
@RequestMapping("/api/admin/health-records")
@CrossOrigin("*")
public class HealthRecordController {

    private final HealthRecordService healthRecordService;
    private final TokenAuthorizer authorizer;

    @Autowired
    public HealthRecordController(HealthRecordService healthRecordService, TokenAuthorizer authorizer) {
        this.healthRecordService = healthRecordService;
        this.authorizer = authorizer;
    }

    @GetMapping
    public ResponseEntity<Object> getAllHealthRecords(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader) {
        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(Collections.singletonMap("error", "Unauthorized, token required"));
            }
            String role = authorizer.checkAdminUser(authorizationHeader);
            if (!role.equals("ADMIN")) {
                return ResponseEntity.status(403).body(Collections.singletonMap("error", "Access restricted"));

            }
            List<HealthRecord> healthRecords = healthRecordService.getAllHealthRecords();
            return ResponseEntity.ok(healthRecords);
        }
        catch (UnauthorizedException e) {
            return ResponseEntity.status(401).body(Collections.singletonMap("error", e.getMessage()));
        } catch (TokenExpiredException e) {
            return ResponseEntity.status(403).body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Error updating user details: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<Object> createHealthRecord(@Valid @RequestBody HealthRecord healthRecord, BindingResult bindingResult, @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader) {
        try{
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(Collections.singletonMap("error", "Unauthorized, token required"));
            }
            String role = authorizer.checkAdminUser(authorizationHeader);
if(!role.equals("ADMIN")){
    return ResponseEntity.status(403).body(Collections.singletonMap("error", "Access restricted"));

}

            if (bindingResult.hasErrors()) {
                Map<String, String> errors = new HashMap<>();
                for (FieldError fieldError : bindingResult.getFieldErrors()) {
                    errors.put("error", fieldError.getDefaultMessage());
                }

                return ResponseEntity.badRequest().body(errors);
            }
            HealthRecord savedHealthRecord = healthRecordService.saveHealthRecord(healthRecord);
            return ResponseEntity.ok(Collections.singletonMap("success","Record creted"));
        }
        catch (UnauthorizedException e) {
            return ResponseEntity.status(401).body(Collections.singletonMap("error", e.getMessage()));
        } catch (TokenExpiredException e) {
            return ResponseEntity.status(403).body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Error updating user details: " + e.getMessage()));
        }
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<Object> getHealthRecordsByEmail(@PathVariable String email,@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader) {
        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(Collections.singletonMap("error", "Unauthorized, token required"));
            }
            String role = authorizer.checkAdminUser(authorizationHeader);
            String userEmail;
            if(authorizer.checkAdminUser(authorizationHeader).equals("ADMIN")){
                userEmail=email;
            }
            else{
                userEmail = authorizer.checkUserAndSendEmail(authorizationHeader);

            }

            List<HealthRecord> healthRecords = healthRecordService.getHealthRecordsByEmail(userEmail);
            return ResponseEntity.ok().body(healthRecords);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(401).body(Collections.singletonMap("error", e.getMessage()));
        } catch (TokenExpiredException e) {
            return ResponseEntity.status(403).body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Error updating user details: " + e.getMessage()));
        }
    }


    @GetMapping("/user/{email}/vitals")
    public ResponseEntity<Object> getHealthRecordsVitalsByEmail(@PathVariable String email,@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader) {
        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(Collections.singletonMap("error", "Unauthorized, token required"));
            }
            String role = authorizer.checkAdminUser(authorizationHeader);
            String userEmail;
            if(authorizer.checkAdminUser(authorizationHeader).equals("ADMIN")){
                userEmail=email;
            }
            else{
                userEmail = authorizer.checkUserAndSendEmail(authorizationHeader);

            }

            List<Map<String,String>> healthRecords = healthRecordService.getUserHealthRecordVitals(userEmail);
            return ResponseEntity.ok().body(healthRecords);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(401).body(Collections.singletonMap("error", e.getMessage()));
        } catch (TokenExpiredException e) {
            return ResponseEntity.status(403).body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Error updating user details: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateHealthRecord(
            @PathVariable String id,
            @RequestBody HealthRecordUpdateRequest updateRequest,@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader) {
        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(Collections.singletonMap("error", "Unauthorized, token required"));
            }
            String role = authorizer.checkAdminUser(authorizationHeader);
            if(!role.equals("ADMIN")){
                return ResponseEntity.status(403).body(Collections.singletonMap("error", "Access restricted"));

            }
            HealthRecord successMessage = healthRecordService.updateHealthRecordById(id, updateRequest);
            return ResponseEntity.ok(successMessage);
        }  catch (UnauthorizedException e) {
            return ResponseEntity.status(401).body(Collections.singletonMap("error", e.getMessage()));
        } catch (TokenExpiredException e) {
            return ResponseEntity.status(403).body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Error updating user details: " + e.getMessage()));
        }
    }



}
