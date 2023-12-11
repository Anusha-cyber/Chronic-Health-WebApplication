package com.smqa.smqa.appointments;


import com.smqa.smqa.requests.AppointmentRequest;
import com.smqa.smqa.tokenauthorizer.TokenAuthorizer;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final TokenAuthorizer authorizer;

    @Autowired
    public AppointmentController(AppointmentService appointmentService,TokenAuthorizer authorizer ) {
        this.appointmentService = appointmentService;
        this.authorizer=authorizer;
    }

    @GetMapping
    public ResponseEntity<Object> getAllAppointments(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader) {
        try{

            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(Collections.singletonMap("error", "Unauthorized, token required"));
            }
            String Role = authorizer.checkAdminUser(authorizationHeader);
            if(!Role.equals("ADMIN")){
                return ResponseEntity.status(403).body(Collections.singletonMap("error","Request not allowed"));

            }
            List<Appointment> appointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointments);
    }catch (Exception e){
            return ResponseEntity.badRequest().body(Collections.singletonMap("error",e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<Object> createAppointment(@Valid @RequestBody AppointmentRequest appointmentRequest,@RequestParam(required = false) String emergency ,@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader) {
        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(Collections.singletonMap("error", "Unauthorized, token required"));
            }

            String email = authorizer.checkUserAndSendEmail(authorizationHeader);
            Appointment appointment;
            if(emergency!=null && !emergency.isEmpty()){
                appointment = appointmentService.createEmergencyAppointment(email, appointmentRequest.getDate());
            }
            else{
                appointment = appointmentService.createAppointment(email, appointmentRequest.getDate());
            }
            return ResponseEntity.ok(appointment);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error",e.getMessage()));

        }
    }

    @PutMapping("/{appointmentId}/status")
    public ResponseEntity<Object> updateAppointmentStatus(@PathVariable String appointmentId,@RequestParam AppointmentStatus newStatus,@RequestParam(required = false) String date,@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader) {
        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(Collections.singletonMap("error", "Unauthorized, token required"));
            }
            String role = authorizer.checkAdminUser(authorizationHeader);
            if(role.equals("USER") && !newStatus.equals(AppointmentStatus.CANCELED)){
                return ResponseEntity.status(400).body(Collections.singletonMap("error", "Request not allowed"));

            }
            if(role.equals("ADMIN") && newStatus.equals(AppointmentStatus.CANCELED)){
                return ResponseEntity.status(400).body(Collections.singletonMap("error", "You cannot cancel this request"));

            }

            appointmentService.updateAppointmentStatus(appointmentId, newStatus);
            if(newStatus.equals(AppointmentStatus.RESCHEDULED)){
                appointmentService.updateAppointmentDate(appointmentId,     LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME));

            }
            return ResponseEntity.ok("Appointment status updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }
    @PutMapping("/{appointmentId}/date")
    public ResponseEntity<Object> updateAppointmentDate(@PathVariable String appointmentId, @RequestParam String date, @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader) {
        try {
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(Collections.singletonMap("error", "Unauthorized, token required"));
            }

            String email = authorizer.checkUserAndSendEmail(authorizationHeader);
            appointmentService.updateAppointmentDate(appointmentId,     LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME));
            return ResponseEntity.ok("Appointment date updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @GetMapping("/{email}")
    public ResponseEntity<Object> getAppointmentsByUserEmail(@PathVariable String email,@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader) {
        try {
            String confEmail;
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(401).body(Collections.singletonMap("error", "Unauthorized, token required"));
            }

                String Role = authorizer.checkAdminUser(authorizationHeader);
                if(Role.equals("ADMIN")){
                    confEmail=email;
                }
                else {
                    confEmail = authorizer.checkUserAndSendEmail(authorizationHeader);
                }
                List<Appointment> appointments = appointmentService.getAllAppointmentsByUserEmail(confEmail);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error retrieving appointments by user email: " + e.getMessage());
        }
    }

}
