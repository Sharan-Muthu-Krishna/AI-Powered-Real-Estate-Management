package com.realestate.management.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.realestate.management.entity.Inquiry;
import com.realestate.management.entity.Property;
import com.realestate.management.service.InquiryService;
import com.realestate.management.service.PropertyService;

@Controller
@RequestMapping("/agent")
public class AgentController {

    private final PropertyService propertyService;
    private final InquiryService inquiryService;

    // ---- Constructor Injection (replaces @RequiredArgsConstructor) ---- //
    public AgentController(PropertyService propertyService,
                           InquiryService inquiryService) {
        this.propertyService = propertyService;
        this.inquiryService = inquiryService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<Property> properties = propertyService.getAllProperties();
        List<Inquiry> inquiries = inquiryService.getAllInquiries();

        model.addAttribute("totalProperties", properties.size());
        model.addAttribute("totalInquiries", inquiries.size());
        model.addAttribute("properties", properties);

        return "agent/dashboard";
    }

    @GetMapping("/properties")
    public String myProperties(Model model) {
        List<Property> properties = propertyService.getAllProperties();
        model.addAttribute("properties", properties);
        return "agent/my-properties";
    }

    @GetMapping("/inquiries")
    public String myInquiries(Model model) {
        List<Inquiry> inquiries = inquiryService.getAllInquiries();
        model.addAttribute("inquiries", inquiries);
        return "agent/my-inquiries";
    }
}
