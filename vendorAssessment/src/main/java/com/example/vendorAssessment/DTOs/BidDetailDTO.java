package com.example.vendorAssessment.DTOs;

import com.example.vendorAssessment.entities.BidClassification;

import java.util.List;

public class BidDetailDTO extends BidDTO {
    private List<BidClassification> classifications;
    private ContractDTO contract;
    private EmailDTO email;

    // Getters and Setters
    public List<BidClassification> getClassifications() { return classifications; }
    public void setClassifications(List<BidClassification> classifications) { this.classifications = classifications; }

    public ContractDTO getContract() { return contract; }
    public void setContract(ContractDTO contract) { this.contract = contract; }

    public EmailDTO getEmail() { return email; }
    public void setEmail(EmailDTO email) { this.email = email; }
}
