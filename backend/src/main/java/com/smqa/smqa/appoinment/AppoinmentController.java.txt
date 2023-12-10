import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;

    @DeleteMapping("/{id}")
    public void cancelAppointment(@PathVariable String id) {
        appointmentService.cancelAppointment(id);
    }
    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        Resource resource = fileDownloadService.downloadFile(fileName);

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
            .body(resource);
    }
    @PutMapping("/{id}")
    public MedicalRecord updateMedicalRecord(@PathVariable String id, @RequestBody MedicalRecord updatedMedicalRecord) {
        return medicalRecordService.updateMedicalRecord(id, updatedMedicalRecord);
    }
    @GetMapping("/user/{userId}/medical-records")
    public List<MedicalRecord> getUserMedicalRecords(@PathVariable String userId) {
        return medicalRecordService.getUserMedicalRecords(userId);
    }
    @PostMapping("/send-notification")
    public void sendNotification(@RequestParam String title, @RequestParam String message) {
        notificationService.sendNotification(title, message);
    }

    @GetMapping("/notifications")
    public List<Notification> getAllNotifications() {
        return notificationService.getAllNotifications();
    }
    @PostMapping("/send-message")
    public void sendMessage(@RequestParam String senderId, @RequestParam String receiverId, @RequestParam String content) {
        messageService.sendMessage(senderId, receiverId, content);
    }

    @GetMapping("/messages")
    public List<Message> getMessages(@RequestParam String senderId, @RequestParam String receiverId) {
        return messageService.getMessages(senderId, receiverId);
    }
}
}
