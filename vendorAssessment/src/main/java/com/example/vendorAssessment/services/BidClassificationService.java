package com.example.vendorAssessment.services;

import com.example.vendorAssessment.entities.Bid;
import com.example.vendorAssessment.entities.BidClassification;
import com.example.vendorAssessment.entities.ClassificationType;
import com.example.vendorAssessment.entities.Email;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.vendorAssessment.repositories.BidClassificationRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
@Transactional
public class BidClassificationService {

    @Autowired
    private BidClassificationRepository classificationRepository;

    // Classification rules and patterns
    private static final Map<String, String> PROJECT_TYPE_KEYWORDS = Map.of(
            "CONSTRUCTION", "construction|building|infrastructure|renovation|repair",
            "IT_SERVICES", "software|technology|system|application|development|programming",
            "CONSULTING", "consulting|advisory|analysis|strategy|planning",
            "MAINTENANCE", "maintenance|support|service|upkeep|repair",
            "SUPPLY", "supply|equipment|materials|procurement|purchase"
    );

    private static final Map<String, Double[]> VALUE_RANGES = Map.of(
            "SMALL", new Double[]{0.0, 50000.0},
            "MEDIUM", new Double[]{50000.0, 500000.0},
            "LARGE", new Double[]{500000.0, 5000000.0},
            "ENTERPRISE", new Double[]{5000000.0, Double.MAX_VALUE}
    );

    private static final Map<String, String> DEPARTMENT_KEYWORDS = Map.of(
            "IT", "technology|software|system|computer|network",
            "FACILITIES", "building|facility|maintenance|cleaning|security",
            "HR", "human resources|recruitment|training|personnel",
            "FINANCE", "financial|accounting|audit|budget",
            "OPERATIONS", "operations|logistics|supply chain|procurement"
    );

    public void classifyBid(Bid bid, Email email) {
        String text = extractTextForClassification(bid, email);

        // Classify by project type
        classifyByProjectType(bid, text);

        // Classify by value range
        classifyByValueRange(bid);

        // Classify by department
        classifyByDepartment(bid, text);

        // Classify by priority (based on amount and keywords)
        classifyByPriority(bid, text);

        // Classify by geographic region (if extractable)
        classifyByGeographicRegion(bid, text);
    }

    private String extractTextForClassification(Bid bid, Email email) {
        StringBuilder text = new StringBuilder();

        if (bid.getProjectName() != null) {
            text.append(bid.getProjectName()).append(" ");
        }

        if (email.getSubject() != null) {
            text.append(email.getSubject()).append(" ");
        }

        if (email.getBody() != null) {
            text.append(email.getBody()).append(" ");
        }

        return text.toString().toLowerCase();
    }

    private void classifyByProjectType(Bid bid, String text) {
        for (Map.Entry<String, String> entry : PROJECT_TYPE_KEYWORDS.entrySet()) {
            Pattern pattern = Pattern.compile("\\b(" + entry.getValue() + ")\\b", Pattern.CASE_INSENSITIVE);
            if (pattern.matcher(text).find()) {
                BidClassification classification = new BidClassification(
                        ClassificationType.PROJECT_TYPE,
                        entry.getKey(),
                        0.8 // confidence score
                );
                classification.setBid(bid);
                classificationRepository.save(classification);
                break; // Take first match
            }
        }
    }

    private void classifyByValueRange(Bid bid) {
        if (bid.getBidAmount() == null) {
            return;
        }

        Double amount = bid.getBidAmount();

        for (Map.Entry<String, Double[]> entry : VALUE_RANGES.entrySet()) {
            Double[] range = entry.getValue();
            if (amount >= range[0] && amount < range[1]) {
                BidClassification classification = new BidClassification(
                        ClassificationType.VALUE_RANGE,
                        entry.getKey(),
                        1.0 // high confidence for amount-based classification
                );
                classification.setBid(bid);
                classificationRepository.save(classification);
                break;
            }
        }
    }

    private void classifyByDepartment(Bid bid, String text) {
        for (Map.Entry<String, String> entry : DEPARTMENT_KEYWORDS.entrySet()) {
            Pattern pattern = Pattern.compile("\\b(" + entry.getValue() + ")\\b", Pattern.CASE_INSENSITIVE);
            if (pattern.matcher(text).find()) {
                BidClassification classification = new BidClassification(
                        ClassificationType.DEPARTMENT,
                        entry.getKey(),
                        0.7
                );
                classification.setBid(bid);
                classificationRepository.save(classification);
                break;
            }
        }
    }

    private void classifyByPriority(Bid bid, String text) {
        String priority = "NORMAL";
        double confidence = 0.6;

        // High priority indicators
        if (text.contains("urgent") || text.contains("immediate") || text.contains("asap")) {
            priority = "HIGH";
            confidence = 0.9;
        } else if (text.contains("critical") || text.contains("emergency")) {
            priority = "CRITICAL";
            confidence = 0.95;
        } else if (bid.getBidAmount() != null && bid.getBidAmount() > 1000000) {
            priority = "HIGH";
            confidence = 0.8;
        } else if (text.contains("low priority") || text.contains("when convenient")) {
            priority = "LOW";
            confidence = 0.8;
        }

        BidClassification classification = new BidClassification(
                ClassificationType.PRIORITY,
                priority,
                confidence
        );
        classification.setBid(bid);
        classificationRepository.save(classification);
    }

    private void classifyByGeographicRegion(Bid bid, String text) {
        // Simple geographic classification based on common location keywords
        Map<String, String> regions = Map.of(
                "NORTH", "north|northern|alaska|canada",
                "SOUTH", "south|southern|florida|texas|georgia",
                "EAST", "east|eastern|new york|boston|philadelphia",
                "WEST", "west|western|california|oregon|washington",
                "CENTRAL", "central|midwest|chicago|kansas|nebraska"
        );

        for (Map.Entry<String, String> entry : regions.entrySet()) {
            Pattern pattern = Pattern.compile("\\b(" + entry.getValue() + ")\\b", Pattern.CASE_INSENSITIVE);
            if (pattern.matcher(text).find()) {
                BidClassification classification = new BidClassification(
                        ClassificationType.GEOGRAPHIC_REGION,
                        entry.getKey(),
                        0.6
                );
                classification.setBid(bid);
                classificationRepository.save(classification);
                break;
            }
        }
    }

    public List<BidClassification> getClassificationsForBid(Long bidId) {
        return classificationRepository.findByBidId(bidId);
    }

    public Map<ClassificationType, List<String>> getAllClassificationValues() {
        Map<ClassificationType, List<String>> result = new HashMap<>();

        for (ClassificationType type : ClassificationType.values()) {
            List<String> values = classificationRepository.findDistinctValuesByType(type);
            result.put(type, values);
        }

        return result;
    }

    public void reclassifyBid(Bid bid, Email email) {
        // Remove existing classifications
        List<BidClassification> existing = classificationRepository.findByBidId(bid.getId());
        classificationRepository.deleteAll(existing);

        // Re-classify
        classifyBid(bid, email);
    }
}
