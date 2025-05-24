package com.example.vendorAssessment.DTOs;

import com.example.vendorAssessment.entities.ContractStatus;

public class ContractStatusUpdateDTO {
    private ContractStatus status;

    public ContractStatus getStatus() { return status; }
    public void setStatus(ContractStatus status) { this.status = status; }
}
