package com.example.vendorAssessment.DTOs;

import com.example.vendorAssessment.entities.BidStatus;
import com.example.vendorAssessment.entities.BidType;

import java.time.LocalDateTime;

public class BidDTO {
    private Long id;
    private String bidNumber;
    private String projectName;
    private String contractorName;
    private String contractorEmail;
    private BidType bidType;
    private BidStatus status;
    private Double bidAmount;
    private LocalDateTime submissionDate;
    private LocalDateTime dueDate;
    private Long emailId;

    // Constructors, getters, setters
    public BidDTO() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getBidNumber() { return bidNumber; }
    public void setBidNumber(String bidNumber) { this.bidNumber = bidNumber; }

    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }

    public String getContractorName() { return contractorName; }
    public void setContractorName(String contractorName) { this.contractorName = contractorName; }

    public String getContractorEmail() { return contractorEmail; }
    public void setContractorEmail(String contractorEmail) { this.contractorEmail = contractorEmail; }

    public BidType getBidType() { return bidType; }
    public void setBidType(BidType bidType) { this.bidType = bidType; }

    public BidStatus getStatus() { return status; }
    public void setStatus(BidStatus status) { this.status = status; }

    public Double getBidAmount() { return bidAmount; }
    public void setBidAmount(Double bidAmount) { this.bidAmount = bidAmount; }

    public LocalDateTime getSubmissionDate() { return submissionDate; }
    public void setSubmissionDate(LocalDateTime submissionDate) { this.submissionDate = submissionDate; }

    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }

    public Long getEmailId() { return emailId; }
    public void setEmailId(Long emailId) { this.emailId = emailId; }
}

