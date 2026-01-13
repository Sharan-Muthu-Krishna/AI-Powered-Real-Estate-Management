package com.realestate.management.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.realestate.management.entity.User;
import com.realestate.management.service.InquiryService;
import com.realestate.management.service.UserService;

@Controller
public class InquiryController {

    private final InquiryService inquiryService;
    private final UserService userService;

    // ---- Constructor Injection (replaces @RequiredArgsConstructor) ---- //
    public InquiryController(InquiryService inquiryService, UserService userService) {
        this.inquiryService = inquiryService;
        this.userService = userService;
    }

    @PostMapping("/inquiries/create")
    public String createInquiry(
            @RequestParam Long propertyId,
            @RequestParam String message,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        try {
            User user = userService.getUserByEmail(authentication.getName());
            inquiryService.createInquiry(user.getId(), propertyId, message);
            redirectAttributes.addFlashAttribute("success", "Inquiry submitted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to submit inquiry: " + e.getMessage());
        }

        return "redirect:/properties/" + propertyId;
    }

    @PostMapping("/inquiries/update-status")
    public String updateInquiryStatus(
            @RequestParam Long id,
            @RequestParam String status,
            @RequestParam(required = false) String returnUrl,
            RedirectAttributes redirectAttributes) {

        try {
            inquiryService.updateInquiryStatus(id, status);
            redirectAttributes.addFlashAttribute("success", "Inquiry status updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update inquiry: " + e.getMessage());
        }

        return "redirect:" + (returnUrl != null ? returnUrl : "/admin/dashboard");
    }
}
