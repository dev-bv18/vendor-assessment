package com.example.vendorAssessment.repositories;

import com.example.vendorAssessment.entities.Bid;
import com.example.vendorAssessment.entities.BidStatus;
import com.example.vendorAssessment.entities.BidType;
import com.example.vendorAssessment.entities.ClassificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
    Optional<Bid> findByBidNumber(String bidNumber);
    List<Bid> findByStatus(BidStatus status);
    List<Bid> findByBidType(BidType bidType);
    List<Bid> findByContractorEmailIgnoreCase(String contractorEmail);
    List<Bid> findByProjectNameContainingIgnoreCase(String projectName);

    List<Bid> findByProjectNameContainingIgnoreCaseOrContractorEmailIgnoreCase(
            String projectName, String contractorEmail
    );

    @Query("SELECT b FROM Bid b WHERE b.bidAmount BETWEEN :minAmount AND :maxAmount")
    List<Bid> findByAmountRange(@Param("minAmount") Double minAmount, @Param("maxAmount") Double maxAmount);

    @Query("SELECT b FROM Bid b JOIN b.classifications c WHERE c.type = :type AND c.value = :value")
    List<Bid> findByClassification(@Param("type") ClassificationType type, @Param("value") String value);
    @Query(value = "SELECT * FROM bids b WHERE " +
            "(:projectName IS NULL OR :projectName = '' OR LOWER(b.project_name) ILIKE LOWER(CONCAT('%', :projectName, '%'))) AND " +
            "(:contractorName IS NULL OR :contractorName = '' OR LOWER(b.contractor_name) ILIKE LOWER(CONCAT('%', :contractorName, '%'))) AND " +
            "(:status IS NULL OR b.status::text = :status) AND " +
            "(:bidType IS NULL OR b.bid_type::text = :bidType) " +
            "order by b.submission_date desc",  // fix here
            countQuery = "SELECT COUNT(*) FROM bids b WHERE " +
                    "(:projectName IS NULL OR :projectName = '' OR LOWER(b.project_name) ILIKE LOWER(CONCAT('%', :projectName, '%'))) AND " +
                    "(:contractorName IS NULL OR :contractorName = '' OR LOWER(b.contractor_name) ILIKE LOWER(CONCAT('%', :contractorName, '%'))) AND " +
                    "(:status IS NULL OR b.status::text = :status) AND " +
                    "(:bidType IS NULL OR b.bid_type::text = :bidType)",
            nativeQuery = true)
    Page<Bid> findBidsWithFilters(
            @Param("projectName") String projectName,
            @Param("contractorName") String contractorName,
            @Param("status") String status,
            @Param("bidType") String bidType,
            Pageable pageable
    );


}