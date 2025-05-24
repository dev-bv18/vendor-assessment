package com.example.vendorAssessment.DTOs;



// Processing Result DTOs


import java.util.List;

public class BatchProcessingResultDTO {
    private int totalProcessed;
    private int bidsIdentified;
    private int errors;
    private List<String> errorMessages;

    // Constructors, getters, setters
    public BatchProcessingResultDTO() {}

    public BatchProcessingResultDTO(int totalProcessed, int bidsIdentified, int errors) {
        this.totalProcessed = totalProcessed;
        this.bidsIdentified = bidsIdentified;
        this.errors = errors;
    }

    // Getters and Setters
    public int getTotalProcessed() { return totalProcessed; }
    public void setTotalProcessed(int totalProcessed) { this.totalProcessed = totalProcessed; }

    public int getBidsIdentified() { return bidsIdentified; }
    public void setBidsIdentified(int bidsIdentified) { this.bidsIdentified = bidsIdentified; }

    public int getErrors() { return errors; }
    public void setErrors(int errors) { this.errors = errors; }

    public List<String> getErrorMessages() { return errorMessages; }
    public void setErrorMessages(List<String> errorMessages) { this.errorMessages = errorMessages; }
}
