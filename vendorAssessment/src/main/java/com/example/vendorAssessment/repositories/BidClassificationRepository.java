package com.example.vendorAssessment.repositories;

import com.example.vendorAssessment.entities.BidClassification;
import com.example.vendorAssessment.entities.ClassificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidClassificationRepository extends JpaRepository<BidClassification, Long> {
    List<BidClassification> findByBidId(Long bidId);
    List<BidClassification> findByType(ClassificationType type);
    List<BidClassification> findByTypeAndValue(ClassificationType type, String value);

    @Query("SELECT DISTINCT c.value FROM BidClassification c WHERE c.type = :type")
    List<String> findDistinctValuesByType(@Param("type") ClassificationType type);
}