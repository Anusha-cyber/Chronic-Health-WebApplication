@RestController
@RequestMapping("/records")
public class RecordController {
    @Autowired
    private RecordService recordService;

    @PutMapping("/{recordId}/prescription-files")
    public ResponseEntity<Record> updatePrescriptionFiles(@PathVariable Long recordId,
                                                         @RequestBody List<PrescriptionFile> newPrescriptionFiles) {
        Record updatedRecord = recordService.updatePrescriptionFiles(recordId, newPrescriptionFiles);
        return new ResponseEntity<>(updatedRecord, HttpStatus.OK);
    }
}
