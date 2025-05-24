package com.example.vendorAssessment.DTOs;

import com.example.vendorAssessment.entities.EmailStatus;

import java.time.LocalDateTime;

public class EmailDTO {
    private Long id;
    private String messageId;
    private String subject;
    private String sender;
    private String recipient;
    private String body;
    private LocalDateTime receivedDate;
    private EmailStatus status;

    // Constructors, getters, setters
    public EmailDTO() {}

    public EmailDTO(String messageId, String subject, String sender, String recipient, String body, LocalDateTime receivedDate) {
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
}
