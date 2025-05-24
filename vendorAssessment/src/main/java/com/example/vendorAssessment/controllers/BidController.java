package com.example.vendorAssessment.controllers;


import com.example.vendorAssessment.DTOs.BidDTO;
import com.example.vendorAssessment.DTOs.BidDashboardDTO;
import com.example.vendorAssessment.DTOs.BidDetailDTO;
import com.example.vendorAssessment.DTOs.BidStatusUpdateDTO;
import com.example.vendorAssessment.entities.BidClassification;
import com.example.vendorAssessment.entities.BidStatus;
import com.example.vendorAssessment.entities.BidType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.vendorAssessment.services.BidClassificationService;
import com.example.vendorAssessment.services.BidService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bids")
@CrossOrigin(origins = "http://localhost:3001")
public class BidController {

    @Autowired
    private BidService bidService;

    @Autowired
    private BidClassificationService classificationService;

    @GetMapping
    public ResponseEntity<Page<BidDTO>> getAllBids(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "submissionDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(required = false) String projectName,
            @RequestParam(required = false) String contractorName,
            @RequestParam(required = false) BidStatus status,
            @RequestParam(required = false) BidType bidType) {


        Pageable pageable = PageRequest.of(page, size);

        // Convert BidStatus enum to string
        String statusStr = (status != null) ? status.name() : null;

        Page<BidDTO> bids = bidService.findBidsWithFilters(projectName, contractorName, statusStr, bidType, pageable);
        return ResponseEntity.ok(bids);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BidDetailDTO> getBidById(@PathVariable Long id) {
        Optional<BidDetailDTO> bid = bidService.findBidById(id);
        return bid.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/classifications")
    public ResponseEntity<List<BidClassification>> getBidClassifications(@PathVariable Long id) {
        List<BidClassification> classifications = classificationService.getClassificationsForBid(id);
        return ResponseEntity.ok(classifications);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<BidDTO> updateBidStatus(@PathVariable Long id, @RequestBody BidStatusUpdateDTO statusUpdate) {
        Optional<BidDTO> updatedBid = bidService.updateBidStatus(id, statusUpdate.getStatus());
        return updatedBid.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<BidDTO>> searchBids(@RequestParam String query) {
        List<BidDTO> bids = bidService.searchBids(query);
        return ResponseEntity.ok(bids);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<BidDashboardDTO> getDashboardData() {
        BidDashboardDTO dashboard = bidService.getDashboardData();
        return ResponseEntity.ok(dashboard);
    }
}
