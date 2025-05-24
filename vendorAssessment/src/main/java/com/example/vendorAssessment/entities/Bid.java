package com.example.vendorAssessment.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bids")
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String bidNumber;

    private String projectName;
    private String contractorName;
    private String contractorEmail;

    @Enumerated(EnumType.STRING)
    private BidType bidType;

    @Enumerated(EnumType.STRING)
    private BidStatus status = BidStatus.RECEIVED;

    private Double bidAmount;
    @Column(name = "submission_date")
    private LocalDateTime submissionDate;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @ManyToOne
    @JoinColumn(name = "email_id")
    private Email email;

    @OneToMany(mappedBy = "bid", cascade = CascadeType.ALL)
    private List<BidClassification> classifications;

    @OneToOne(mappedBy = "bid", cascade = CascadeType.ALL)
    private Contract contract;

    // Constructors, getters, setters
    public Bid() {}

    public Bid(String bidNumber, String projectName, String contractorName, String contractorEmail) {
        this.bidNumber = bidNumber;
        this.projectName = projectName;
        this.contractorName = contractorName;
        this.contractorEmail = contractorEmail;
    }

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

    public Email getEmail() { return email; }
    public void setEmail(Email email) { this.email = email; }

    public List<BidClassification> getClassifications() { return classifications; }
    public void setClassifications(List<BidClassification> classifications) { this.classifications = classifications; }

    public com.example.vendorAssessment.entities.Contract getContract() { return contract; }
    public void setContract(Contract contract) { this.contract = contract; }
}

