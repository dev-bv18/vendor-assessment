package com.example.vendorAssessment.DTOs;

import com.example.vendorAssessment.entities.BidStatus;

public class BidStatusUpdateDTO {
    private BidStatus status;

    // Setter
    public void setStatus(BidStatus status) {
        this.status = status;
    }

    public BidStatus getStatus() {
        return status;
    }
}




