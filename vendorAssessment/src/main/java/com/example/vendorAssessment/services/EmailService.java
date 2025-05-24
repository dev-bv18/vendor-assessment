package com.example.vendorAssessment.services;

import com.example.vendorAssessment.DTOs.BatchProcessingResultDTO;
import com.example.vendorAssessment.DTOs.BidDTO;
import com.example.vendorAssessment.DTOs.EmailDTO;
import com.example.vendorAssessment.DTOs.EmailProcessingResultDTO;
import com.example.vendorAssessment.entities.Bid;
import com.example.vendorAssessment.entities.Email;
import com.example.vendorAssessment.entities.EmailStatus;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.vendorAssessment.repositories.EmailRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmailService {

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private BidRecognitionService bidRecognitionService;

    public EmailProcessingResultDTO processEmail(EmailDTO emailDTO) {
        try {
            // Convert DTO to entity
            Email email = convertDTOToEntity(emailDTO);

            // Check if email already exists
            Optional<Email> existingEmail = emailRepository.findByMessageId(email.getMessageId());
            if (existingEmail.isPresent()) {
                return new EmailProcessingResultDTO(false, "Email already processed");
            }

            // Save email
            email = emailRepository.save(email);

            // Process for bid recognition
            Bid bid = bidRecognitionService.processAndCreateBid(email);

            EmailProcessingResultDTO result = new EmailProcessingResultDTO(true, "Email processed successfully");

            if (bid != null) {
                result.setBidIdentified(true);
                result.setBid(convertBidToDTO(bid));
            }

            return result;

        } catch (Exception e) {
            EmailProcessingResultDTO result = new EmailProcessingResultDTO(false, "Error processing email: " + e.getMessage());
            result.setErrors(Arrays.asList(e.getMessage()));
            return result;
        }
    }

    public List<EmailDTO> getAllEmails(int page, int size) {
        List<Email> emails = emailRepository.findAll();
        return emails.stream()
                .skip((long) page * size)
                .limit(size)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<EmailDTO> getUnprocessedEmails() {
        List<Email> emails = emailRepository.findByStatus(EmailStatus.UNPROCESSED);
        return emails.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public BatchProcessingResultDTO batchProcessUnprocessedEmails() {
        List<Email> unprocessedEmails = emailRepository.findByStatus(EmailStatus.UNPROCESSED);

        int totalProcessed = 0;
        int bidsIdentified = 0;
        int errors = 0;
        List<String> errorMessages = new ArrayList<>();

        for (Email email : unprocessedEmails) {
            try {
                Bid bid = bidRecognitionService.processAndCreateBid(email);
                totalProcessed++;

                if (bid != null) {
                    bidsIdentified++;
                }

            } catch (Exception e) {
                errors++;
                errorMessages.add("Error processing email " + email.getId() + ": " + e.getMessage());
            }
        }

        BatchProcessingResultDTO result = new BatchProcessingResultDTO(totalProcessed, bidsIdentified, errors);
        result.setErrorMessages(errorMessages);

        return result;
    }

    private Email convertDTOToEntity(EmailDTO dto) {
        Email email = new Email();
        email.setMessageId(dto.getMessageId());
        email.setSubject(dto.getSubject());
        email.setSender(dto.getSender());
        email.setRecipient(dto.getRecipient());
        email.setBody(dto.getBody());
        email.setReceivedDate(dto.getReceivedDate());
        email.setStatus(EmailStatus.UNPROCESSED);
        return email;
    }

    private EmailDTO convertToDTO(Email email) {
        EmailDTO dto = new EmailDTO();
        dto.setId(email.getId());
        dto.setMessageId(email.getMessageId());
        dto.setSubject(email.getSubject());
        dto.setSender(email.getSender());
        dto.setRecipient(email.getRecipient());
        dto.setBody(email.getBody());
        dto.setReceivedDate(email.getReceivedDate());
        dto.setStatus(email.getStatus());
        return dto;
    }

    private BidDTO convertBidToDTO(Bid bid) {
        BidDTO dto = new BidDTO();
        dto.setId(bid.getId());
        dto.setBidNumber(bid.getBidNumber());
        dto.setProjectName(bid.getProjectName());
        dto.setContractorName(bid.getContractorName());
        dto.setContractorEmail(bid.getContractorEmail());
        dto.setBidType(bid.getBidType());
        dto.setStatus(bid.getStatus());
        dto.setBidAmount(bid.getBidAmount());
        dto.setSubmissionDate(bid.getSubmissionDate());
        dto.setDueDate(bid.getDueDate());
        return dto;
    }
}

