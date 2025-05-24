package com.example.vendorAssessment.controllers;

import com.example.vendorAssessment.entities.ClassificationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.vendorAssessment.services.BidClassificationService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/classifications")
@CrossOrigin(origins = "http://localhost:3001")
public class ClassificationController {

    @Autowired
    private BidClassificationService classificationService;

    @GetMapping("/values")
    public ResponseEntity<Map<ClassificationType, List<String>>> getAllClassificationValues() {
        Map<ClassificationType, List<String>> values = classificationService.getAllClassificationValues();
        return ResponseEntity.ok(values);
    }
}
