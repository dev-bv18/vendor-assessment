package com.example.vendorAssessment.services;

import com.example.vendorAssessment.entities.Bid;
import com.example.vendorAssessment.entities.BidType;
import com.example.vendorAssessment.entities.Email;
import com.example.vendorAssessment.entities.EmailStatus;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.vendorAssessment.repositories.BidRepository;
import com.example.vendorAssessment.repositories.EmailRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class BidRecognitionService {

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private BidClassificationService classificationService;

    // Bid recognition keywords and patterns
    private static final Set<String> BID_KEYWORDS = Set.of(
            "bid", "proposal", "quotation", "tender", "rfp", "request for proposal",
            "quote", "estimate", "contract", "procurement", "vendor", "supplier"
    );

    private static final Pattern BID_NUMBER_PATTERN = Pattern.compile(
            "(?i)(?:bid|rfp|proposal)\\s*(?:no|number|#)?\\s*:?\\s*([A-Z0-9-]+)"
    );

    private static final Pattern AMOUNT_PATTERN = Pattern.compile(
            "\\$([0-9,]+(?:\\.[0-9]{2})?)"
    );

    public boolean isBidRelatedEmail(Email email) {
        String subject = email.getSubject().toLowerCase();
        String body = email.getBody() != null ? email.getBody().toLowerCase() : "";

        // Check for bid keywords in subject
        for (String keyword : BID_KEYWORDS) {
            if (subject.contains(keyword) || body.contains(keyword)) {
                return true;
            }
        }

        // Check for bid number patterns
        if (BID_NUMBER_PATTERN.matcher(subject + " " + body).find()) {
            return true;
        }

        return false;
    }

    public Bid processAndCreateBid(Email email) {
        if (!isBidRelatedEmail(email)) {
            return null;
        }

        // Extract bid information
        String bidNumber = extractBidNumber(email);
        String projectName = extractProjectName(email);
        String contractorName = extractFromSender(email.getSender());
        Double bidAmount = extractBidAmount(email);

        // Check if bid already exists
        Optional<Bid> existingBid = bidRepository.findByBidNumber(bidNumber);
        if (existingBid.isPresent()) {
            // Link follow-up email to existing bid
            email.getBids().add(existingBid.get());
            emailRepository.save(email);
            return existingBid.get();
        }

        // Create new bid
        Bid bid = new Bid(bidNumber, projectName, contractorName, email.getSender());
        bid.setBidAmount(bidAmount);
        bid.setSubmissionDate(email.getReceivedDate());
        bid.setEmail(email);
        bid.setBidType(determineBidType(email));

        // Save bid
        bid = bidRepository.save(bid);

        // Create classifications
        classificationService.classifyBid(bid, email);

        // Update email status
        email.setStatus(EmailStatus.BID_IDENTIFIED);
        emailRepository.save(email);

        return bid;
    }

    private String extractBidNumber(Email email) {
        String text = email.getSubject() + " " + (email.getBody() != null ? email.getBody() : "");
        Matcher matcher = BID_NUMBER_PATTERN.matcher(text);

        if (matcher.find()) {
            return matcher.group(1);
        }

        // Generate bid number if not found
        return "BID-" + System.currentTimeMillis();
    }

    private String extractProjectName(Email email) {
        String subject = email.getSubject();

        // Try to extract project name from subject
        // Look for patterns like "Project: Name" or "RE: Project Name"
        Pattern projectPattern = Pattern.compile("(?i)(?:project|re)\\s*:?\\s*(.+?)(?:\\s*-|$)");
        Matcher matcher = projectPattern.matcher(subject);

        if (matcher.find()) {
            return matcher.group(1).trim();
        }

        // If no specific pattern, use the subject as project name
        return subject.length() > 100 ? subject.substring(0, 100) + "..." : subject;
    }

    private String extractFromSender(String senderEmail) {
        if (senderEmail == null) return "Unknown";

        // Extract name from email if in format "Name <email@domain>"
        Pattern namePattern = Pattern.compile("^(.+?)\\s*<.+>$");
        Matcher matcher = namePattern.matcher(senderEmail);

        if (matcher.find()) {
            return matcher.group(1).trim();
        }

        // Extract domain name as fallback
        String[] parts = senderEmail.split("@");
        if (parts.length > 1) {
            return parts[1].split("\\.")[0];
        }

        return senderEmail;
    }

    private Double extractBidAmount(Email email) {
        String text = (email.getBody() != null ? email.getBody() : "") + " " + email.getSubject();
        Matcher matcher = AMOUNT_PATTERN.matcher(text);

        if (matcher.find()) {
            try {
                String amountStr = matcher.group(1).replace(",", "");
                return Double.parseDouble(amountStr);
            } catch (NumberFormatException e) {
                // Ignore parsing errors
            }
        }

        return null;
    }

    private BidType determineBidType(Email email) {
        String text = (email.getSubject() + " " + (email.getBody() != null ? email.getBody() : "")).toLowerCase();

        if (text.contains("construction") || text.contains("building") || text.contains("infrastructure")) {
            return BidType.CONSTRUCTION;
        } else if (text.contains("software") || text.contains("it") || text.contains("technology")) {
            return BidType.IT_SERVICES;
        } else if (text.contains("consulting") || text.contains("advisory") || text.contains("service")) {
            return BidType.CONSULTING;
        } else if (text.contains("maintenance") || text.contains("repair") || text.contains("support")) {
            return BidType.MAINTENANCE;
        } else if (text.contains("supply") || text.contains("equipment") || text.contains("material")) {
            return BidType.SUPPLY;
        }

        return BidType.OTHER;
    }

    public List<Bid> findRelatedBids(String projectName, String contractorEmail) {
        return bidRepository.findByProjectNameContainingIgnoreCaseOrContractorEmailIgnoreCase(
                projectName, contractorEmail
        );
    }
}
