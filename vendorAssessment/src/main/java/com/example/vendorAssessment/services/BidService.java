package com.example.vendorAssessment.services;

import com.example.vendorAssessment.DTOs.*;
import com.example.vendorAssessment.entities.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.example.vendorAssessment.repositories.BidClassificationRepository;
import com.example.vendorAssessment.repositories.BidRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BidService {

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private BidClassificationRepository classificationRepository;

    public Page<BidDTO> findBidsWithFilters(String projectName, String contractorName,
                                            String status, BidType bidType, Pageable pageable) {
        // Convert enum parameters to strings for native query
        String bidTypeStr = (bidType != null) ? bidType.name() : null;

        Page<Bid> bids = bidRepository.findBidsWithFilters(projectName, contractorName, status, bidTypeStr, pageable);
        return bids.map(this::convertToDTO);
    }

    public Optional<BidDetailDTO> findBidById(Long id) {
        return bidRepository.findById(id).map(this::convertToDetailDTO);
    }

    public Optional<BidDTO> updateBidStatus(Long id, BidStatus status) {
        return bidRepository.findById(id).map(bid -> {
            bid.setStatus(status);
            bid = bidRepository.save(bid);
            return convertToDTO(bid);
        });
    }

    public List<BidDTO> searchBids(String query) {
        // Simple search implementation - can be enhanced with full-text search
        List<Bid> bids = bidRepository.findByProjectNameContainingIgnoreCase(query);
        return bids.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public BidDashboardDTO getDashboardData() {
        BidDashboardDTO dashboard = new BidDashboardDTO();

        dashboard.setTotalBids(bidRepository.count());
        dashboard.setActiveBids(bidRepository.findByStatus(BidStatus.UNDER_REVIEW).size());
        dashboard.setCompletedBids(bidRepository.findByStatus(BidStatus.ACCEPTED).size() +
                bidRepository.findByStatus(BidStatus.CONTRACTED).size());
        dashboard.setPendingReview(bidRepository.findByStatus(BidStatus.RECEIVED).size());

        // Calculate total value
        List<Bid> allBids = bidRepository.findAll();
        Double totalValue = allBids.stream()
                .filter(bid -> bid.getBidAmount() != null)
                .mapToDouble(Bid::getBidAmount)
                .sum();
        dashboard.setTotalValue(totalValue);

        // Status counts
        List<BidDashboardDTO.BidStatusCount> statusCounts = Arrays.stream(BidStatus.values())
                .map(status -> new BidDashboardDTO.BidStatusCount(status,
                        bidRepository.findByStatus(status).size()))
                .collect(Collectors.toList());
        dashboard.setStatusCounts(statusCounts);

        // Type counts
        List<BidDashboardDTO.BidTypeCount> typeCounts = Arrays.stream(BidType.values())
                .map(type -> new BidDashboardDTO.BidTypeCount(type,
                        bidRepository.findByBidType(type).size()))
                .collect(Collectors.toList());
        dashboard.setTypeCounts(typeCounts);

        return dashboard;
    }

    private BidDTO convertToDTO(Bid bid) {
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
        if (bid.getEmail() != null) {
            dto.setEmailId(bid.getEmail().getId());
        }
        return dto;
    }

    private BidDetailDTO convertToDetailDTO(Bid bid) {
        BidDetailDTO dto = new BidDetailDTO();
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

        if (bid.getEmail() != null) {
            dto.setEmailId(bid.getEmail().getId());
            dto.setEmail(convertEmailToDTO(bid.getEmail()));
        }

        dto.setClassifications(bid.getClassifications());

        if (bid.getContract() != null) {
            dto.setContract(convertContractToDTO(bid.getContract()));
        }

        return dto;
    }

    private EmailDTO convertEmailToDTO(Email email) {
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

    private ContractDTO convertContractToDTO(Contract contract) {
        ContractDTO dto = new ContractDTO();
        dto.setId(contract.getId());
        dto.setContractNumber(contract.getContractNumber());
        dto.setTitle(contract.getTitle());
        dto.setValue(contract.getValue());
        dto.setStartDate(contract.getStartDate());
        dto.setEndDate(contract.getEndDate());
        dto.setStatus(contract.getStatus());
        if (contract.getBid() != null) {
            dto.setBidId(contract.getBid().getId());
        }
        return dto;
    }
}