package com.example.vendorAssessment.entities;


import jakarta.persistence.*;

// BidClassification Entity
@Entity
@Table(name = "bid_classifications")
public class BidClassification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ClassificationType type;

    private String value;
    private Double confidence;

    @ManyToOne
    @JoinColumn(name = "bid_id")
    private Bid bid;

    // Constructors, getters, setters
    public BidClassification() {}

    public BidClassification(ClassificationType type, String value, Double confidence) {
        this.type = type;
        this.value = value;
        this.confidence = confidence;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ClassificationType getType() { return type; }
    public void setType(ClassificationType type) { this.type = type; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public Double getConfidence() { return confidence; }
    public void setConfidence(Double confidence) { this.confidence = confidence; }

    public Bid getBid() { return bid; }
    public void setBid(Bid bid) { this.bid = bid; }
}

