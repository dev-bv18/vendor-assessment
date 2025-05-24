package com.example.vendorAssessment.repositories;

import com.example.vendorAssessment.entities.Email;
import com.example.vendorAssessment.entities.EmailStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmailRepository extends JpaRepository<Email, Long> {
    Optional<Email> findByMessageId(String messageId);
    List<Email> findByStatus(EmailStatus status);
    List<Email> findByReceivedDateBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT e FROM Email e WHERE e.subject LIKE %:keyword% OR e.body LIKE %:keyword%")
    List<Email> findByKeyword(@Param("keyword") String keyword);
}
