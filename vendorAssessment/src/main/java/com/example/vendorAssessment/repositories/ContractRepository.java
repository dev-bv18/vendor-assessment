package com.example.vendorAssessment.repositories;

import com.example.vendorAssessment.entities.Contract;
import com.example.vendorAssessment.entities.ContractStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    Optional<Contract> findByContractNumber(String contractNumber);
    List<Contract> findByStatus(ContractStatus status);
    List<Contract> findByValueBetween(Double minValue, Double maxValue);

    @Query("SELECT c FROM Contract c WHERE c.bid.id = :bidId")
    Optional<Contract> findByBidId(@Param("bidId") Long bidId);
}
