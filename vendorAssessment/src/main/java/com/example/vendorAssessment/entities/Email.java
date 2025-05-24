package com.example.vendorAssessment.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "emails")
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String messageId;

    @Column(nullable = false)
    private String subject;

    private String sender;
    private String recipient;

    @Lob
    private String body;

    @Column(nullable = false)
    private LocalDateTime receivedDate;

    @Enumerated(EnumType.STRING)
    private EmailStatus status = EmailStatus.UNPROCESSED;

    @OneToMany(mappedBy = "email", cascade = CascadeType.ALL)
    private List<Bid> bids;

    // Constructors, getters, setters
    public Email() {}

    public Email(String messageId, String subject, String sender, String recipient, String body, LocalDateTime receivedDate) {
        this.messageId = messageId;
        this.subject = subject;
        this.sender = sender;
        this.recipient = recipient;
        this.body = body;
        this.receivedDate = receivedDate;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public LocalDateTime getReceivedDate() { return receivedDate; }
    public void setReceivedDate(LocalDateTime receivedDate) { this.receivedDate = receivedDate; }

    public EmailStatus getStatus() { return status; }
    public void setStatus(EmailStatus status) { this.status = status; }

    public List<Bid> getBids() { return bids; }
    public void setBids(List<Bid> bids) { this.bids = bids; }
}