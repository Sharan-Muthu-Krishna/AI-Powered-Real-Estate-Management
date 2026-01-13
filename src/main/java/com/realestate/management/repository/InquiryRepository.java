// InquiryRepository.java - src/main/java/com/realestate/management/repository/InquiryRepository.java
package com.realestate.management.repository;

import com.realestate.management.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    List<Inquiry> findByUserId(Long userId);
    List<Inquiry> findByPropertyId(Long propertyId);
    List<Inquiry> findByStatus(String status);
    long countByStatus(String status);
}