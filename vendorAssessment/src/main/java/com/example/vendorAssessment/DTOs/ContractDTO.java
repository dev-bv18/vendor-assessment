package com.example.vendorAssessment.DTOs;

import com.example.vendorAssessment.entities.ContractStatus;

import java.time.LocalDateTime;

// Contract DTOs
public class ContractDTO {
    private Long id;
    private String contractNumber;
    private String title;
    private Double value;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private ContractStatus status;
    private Long bidId;

    // Constructors, getters, setters
    public ContractDTO() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getContractNumber() { return contractNumber; }
    public void setContractNumber(String contractNumber) { this.contractNumber = contractNumber; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Double getValue() { return value; }
    public void setValue(Double value) { this.value = value; }

    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }

    public LocalDateTime getEndDate() { return endDate; }
    public void setEndDate(LocalDateTime endDate) { this.endDate = endDate; }

    public ContractStatus getStatus() { return status; }
    public void setStatus(ContractStatus status) { this.status = status; }

    public Long getBidId() { return bidId; }
    public void setBidId(Long bidId) { this.bidId = bidId; }
}

