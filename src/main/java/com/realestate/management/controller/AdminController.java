package com.realestate.management.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.realestate.management.entity.Inquiry;
import com.realestate.management.entity.Property;
import com.realestate.management.entity.User;
import com.realestate.management.service.FileStorageService;
import com.realestate.management.service.InquiryService;
import com.realestate.management.service.PropertyService;
import com.realestate.management.service.UserService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final PropertyService propertyService;
    private final InquiryService inquiryService;
    private final FileStorageService fileStorageService;

    // ---- Constructor Injection (replaces @RequiredArgsConstructor) ---- //
    public AdminController(UserService userService,
                           PropertyService propertyService,
                           InquiryService inquiryService,
                           FileStorageService fileStorageService) {
        this.userService = userService;
        this.propertyService = propertyService;
        this.inquiryService = inquiryService;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        long totalUsers = userService.getAllUsers().size();
        long totalProperties = propertyService.countProperties();
        long activeInquiries = inquiryService.countActiveInquiries();

        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalProperties", totalProperties);
        model.addAttribute("activeInquiries", activeInquiries);

        List<Property> recentProperties = propertyService.getAllProperties();
        model.addAttribute("recentProperties", recentProperties.size() > 5 ?
                recentProperties.subList(0, 5) : recentProperties);

        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String manageUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/manage-users";
    }

    @PostMapping("/users/update-role")
    public String updateUserRole(@RequestParam Long userId,
                                 @RequestParam String role,
                                 RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserById(userId);
            user.setRole(role);
            userService.updateUser(userId, user);
            redirectAttributes.addFlashAttribute("success", "User role updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update role: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("success", "User deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete user: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/properties")
    public String manageProperties(Model model) {
        List<Property> properties = propertyService.getAllProperties();
        model.addAttribute("properties", properties);
        model.addAttribute("property", new Property());
        return "admin/manage-properties";
    }

    @PostMapping("/properties/create")
    public String createProperty(@Valid @ModelAttribute Property property,
                                 BindingResult result,
                                 @RequestParam(value = "image", required = false) MultipartFile image,
                                 RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Validation failed");
            return "redirect:/admin/properties";
        }

        try {
            if (image != null && !image.isEmpty()) {
                String fileName = fileStorageService.storeFile(image);
                property.setImageUrl("/uploads/properties/" + fileName);
            }
            propertyService.createProperty(property);
            redirectAttributes.addFlashAttribute("success", "Property created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create property: " + e.getMessage());
        }

        return "redirect:/admin/properties";
    }

    @PostMapping("/properties/update/{id}")
    public String updateProperty(@PathVariable Long id,
                                 @Valid @ModelAttribute Property property,
                                 BindingResult result,
                                 @RequestParam(value = "image", required = false) MultipartFile image,
                                 RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Validation failed");
            return "redirect:/admin/properties";
        }

        try {
            if (image != null && !image.isEmpty()) {
                String fileName = fileStorageService.storeFile(image);
                property.setImageUrl("/uploads/properties/" + fileName);
            }
            propertyService.updateProperty(id, property);
            redirectAttributes.addFlashAttribute("success", "Property updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update property: " + e.getMessage());
        }

        return "redirect:/admin/properties";
    }

    @PostMapping("/properties/delete/{id}")
    public String deleteProperty(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            propertyService.deleteProperty(id);
            redirectAttributes.addFlashAttribute("success", "Property deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete property: " + e.getMessage());
        }
        return "redirect:/admin/properties";
    }

    @GetMapping("/inquiries")
    public String manageInquiries(Model model) {
        List<Inquiry> inquiries = inquiryService.getAllInquiries();
        model.addAttribute("inquiries", inquiries);
        return "admin/manage-inquiries";
    }
}
