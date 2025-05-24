package com.example.vendorAssessment.controllers;


import com.example.vendorAssessment.DTOs.BatchProcessingResultDTO;
import com.example.vendorAssessment.DTOs.EmailDTO;
import com.example.vendorAssessment.DTOs.EmailProcessingResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.vendorAssessment.services.EmailService;

import java.util.List;

@RestController
@RequestMapping("/api/emails")
@CrossOrigin(origins = "http://localhost:3001")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/process")
    public ResponseEntity<EmailProcessingResultDTO> processEmail(@RequestBody EmailDTO emailDTO) {
        EmailProcessingResultDTO result = emailService.processEmail(emailDTO);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<List<EmailDTO>> getAllEmails(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<EmailDTO> emails = emailService.getAllEmails(page, size);
        return ResponseEntity.ok(emails);
    }

    @GetMapping("/unprocessed")
    public ResponseEntity<List<EmailDTO>> getUnprocessedEmails() {
        List<EmailDTO> emails = emailService.getUnprocessedEmails();
        return ResponseEntity.ok(emails);
    }

    @PostMapping("/batch-process")
    public ResponseEntity<BatchProcessingResultDTO> batchProcessEmails() {
        BatchProcessingResultDTO result = emailService.batchProcessUnprocessedEmails();
        return ResponseEntity.ok(result);
    }
}
