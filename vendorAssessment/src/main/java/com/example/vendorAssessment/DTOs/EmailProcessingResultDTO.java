package com.example.vendorAssessment.DTOs;

import java.util.List;

public class EmailProcessingResultDTO {
    private boolean success;
    private String message;
    private boolean bidIdentified;
    private BidDTO bid;
    private List<String> errors;

    // Constructors, getters, setters
    public EmailProcessingResultDTO() {}

    public EmailProcessingResultDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // Getters and Setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public boolean isBidIdentified() { return bidIdentified; }
    public void setBidIdentified(boolean bidIdentified) { this.bidIdentified = bidIdentified; }

    public BidDTO getBid() { return bid; }
    public void setBid(BidDTO bid) { this.bid = bid; }

    public List<String> getErrors() { return errors; }
    public void setErrors(List<String> errors) { this.errors = errors; }
}
