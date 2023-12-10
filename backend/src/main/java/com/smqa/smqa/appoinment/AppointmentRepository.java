import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private MongoService mongoService;

    @PostMapping("/reset-password")
    public void resetPassword(@RequestParam String username, @RequestParam String newPassword) {
        mongoService.resetPassword(username, newPassword);
    }
    @GetMapping
    public List<MedicalHistory> getAllMedicalHistory() {
        return medicalHistoryService.getAllMedicalHistory();
    }
    @GetMapping
    public List<Appointment> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }
     
    }
    @PostMapping("/{requestId}/approve")
    public void approveAppointmentRequest(@PathVariable String requestId) {
        appointmentRequestService.approveAppointmentRequest(requestId);
    }
   
}
