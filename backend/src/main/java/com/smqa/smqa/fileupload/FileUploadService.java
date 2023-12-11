package com.smqa.smqa.fileupload;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileUploadService {

    @Value("${upload.path}")
    private String uploadPath;


    public String uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        // Generate a unique filename using UUID
        String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        // Save the file to the upload directory in the working directory
        Path filePath = Paths.get("./uploads", filename);

        try {
            file.transferTo(filePath);
            return filename;
        } catch (IOException e) {
            throw new RuntimeException("Error saving the file", e);
        }
    }
}
