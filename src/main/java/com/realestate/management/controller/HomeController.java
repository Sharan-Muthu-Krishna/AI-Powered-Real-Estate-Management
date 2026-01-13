package com.realestate.management.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.realestate.management.entity.Property;
import com.realestate.management.service.PropertyService;

@Controller
public class HomeController {

    private final PropertyService propertyService;

    // ---- Constructor Injection (replaces @RequiredArgsConstructor) ---- //
    public HomeController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<Property> properties = propertyService.getAllProperties();
        model.addAttribute("properties", properties.size() > 6 ? properties.subList(0, 6) : properties);
        return "public/index";
    }

    @GetMapping("/properties")
    public String properties(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            Model model) {

        List<Property> properties;
        if (type != null || location != null || minPrice != null || maxPrice != null) {
            properties = propertyService.searchProperties(type, location, minPrice, maxPrice);
        } else {
            properties = propertyService.getAllProperties();
        }

        model.addAttribute("properties", properties);
        model.addAttribute("selectedType", type);
        model.addAttribute("selectedLocation", location);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);

        return "public/properties";
    }

    @GetMapping("/properties/{id}")
    public String propertyDetail(@PathVariable Long id, Model model) {
        Property property = propertyService.getPropertyById(id);
        model.addAttribute("property", property);
        return "public/property-detail";
    }
}
