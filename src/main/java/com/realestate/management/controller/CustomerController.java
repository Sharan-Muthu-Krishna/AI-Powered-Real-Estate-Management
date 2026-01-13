package com.realestate.management.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.realestate.management.entity.Inquiry;
import com.realestate.management.entity.User;
import com.realestate.management.service.InquiryService;
import com.realestate.management.service.UserService;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    private final InquiryService inquiryService;
    private final UserService userService;

    // ---- Constructor Injection (replaces @RequiredArgsConstructor) ---- //
    public CustomerController(InquiryService inquiryService, UserService userService) {
        this.inquiryService = inquiryService;
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        User user = userService.getUserByEmail(authentication.getName());
        List<Inquiry> inquiries = inquiryService.getInquiriesByUserId(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("inquiries", inquiries);
        model.addAttribute("totalInquiries", inquiries.size());

        return "customer/dashboard";
    }

    @GetMapping("/inquiries")
    public String myInquiries(Authentication authentication, Model model) {
        User user = userService.getUserByEmail(authentication.getName());
        List<Inquiry> inquiries = inquiryService.getInquiriesByUserId(user.getId());

        model.addAttribute("inquiries", inquiries);
        return "customer/my-inquiries";
    }
}
