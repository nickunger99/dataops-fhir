package com.dataops.api.controller;

import com.dataops.api.domain.MessageResponse;
import com.dataops.api.domain.patient.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @PostMapping(path = "upload-csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN') or hasRole('MODERATOR') or hasRole('USER')")
    public ResponseEntity uploadCSV(
            @RequestParam("csv") @RequestPart(required = true) MultipartFile csv) {
        if (csv.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("CSV is empty"));
        }
        try {
            patientService.savePatientFromCSV(csv);
            return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("CSV processed and patients saved"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Error processing CSV file"));
        }
    }
}
