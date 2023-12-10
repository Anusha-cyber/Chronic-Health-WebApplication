import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/your-entity")
public class YourEntityController {

    @Autowired
    private YourEntityService service;

    @GetMapping("/all")
    public List<YourEntity> getAllRecords() {
        return service.getAllRecords();

    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Appointment>> getAppointmentsForUser(@PathVariable String userId) {
        List<Appointment> appointments = appointmentService.getAppointmentsForUser(userId);
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }
    @PutMapping("/{id}/modify-date")
    public void modifyAppointmentDate(@PathVariable String id, @RequestBody Date newDate) {
        appointmentService.modifyAppointmentDate(id, newDate);
    }
    @GetMapping("/{userId}")
    public List<Appointment> getAppointmentsByUserId(@PathVariable String userId) {
        return appointmentService.getAppointmentsByUserId(userId);
    }
    @PostMapping
    public Appointment createAppointment(@RequestBody Appointment appointment) {
        return appointmentService.createAppointment(appointment);
    }
    @GetMapping
    public List<MedicalRecord> getAllMedicalRecords() {
        return medicalRecordService.getAllMedicalRecords();
    }

    @PostMapping
    public MedicalRecord saveMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        return medicalRecordService.saveMedicalRecord(medicalRecord);
    }
	
}
