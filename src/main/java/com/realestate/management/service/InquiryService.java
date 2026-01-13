package com.realestate.management.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.realestate.management.entity.Inquiry;
import com.realestate.management.entity.Property;
import com.realestate.management.entity.User;
import com.realestate.management.exception.ResourceNotFoundException;
import com.realestate.management.repository.InquiryRepository;

@Service
@Transactional
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final UserService userService;
    private final PropertyService propertyService;

    // ---- Constructor Injection (replaces @RequiredArgsConstructor) ---- //

    public InquiryService(InquiryRepository inquiryRepository,
                          UserService userService,
                          PropertyService propertyService) {
        this.inquiryRepository = inquiryRepository;
        this.userService = userService;
        this.propertyService = propertyService;
    }

    // ---------------- Service Methods ---------------- //

    public Inquiry createInquiry(Long userId, Long propertyId, String message) {
        User user = userService.getUserById(userId);
        Property property = propertyService.getPropertyById(propertyId);

        Inquiry inquiry = new Inquiry();
        inquiry.setUser(user);
        inquiry.setProperty(property);
        inquiry.setMessage(message);
        inquiry.setStatus("PENDING");

        return inquiryRepository.save(inquiry);
    }

    public Inquiry getInquiryById(Long id) {
        return inquiryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inquiry not found with id: " + id));
    }

    public List<Inquiry> getAllInquiries() {
        return inquiryRepository.findAll();
    }

    public List<Inquiry> getInquiriesByUserId(Long userId) {
        return inquiryRepository.findByUserId(userId);
    }

    public List<Inquiry> getInquiriesByPropertyId(Long propertyId) {
        return inquiryRepository.findByPropertyId(propertyId);
    }

    public List<Inquiry> getInquiriesByStatus(String status) {
        return inquiryRepository.findByStatus(status);
    }

    public Inquiry updateInquiryStatus(Long id, String status) {
        Inquiry inquiry = getInquiryById(id);
        inquiry.setStatus(status);
        return inquiryRepository.save(inquiry);
    }

    public void deleteInquiry(Long id) {
        Inquiry inquiry = getInquiryById(id);
        inquiryRepository.delete(inquiry);
    }

    public long countActiveInquiries() {
        return inquiryRepository.countByStatus("PENDING") +
               inquiryRepository.countByStatus("CONTACTED");
    }
}
