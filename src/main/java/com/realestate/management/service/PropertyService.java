package com.realestate.management.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.realestate.management.entity.Property;
import com.realestate.management.exception.ResourceNotFoundException;
import com.realestate.management.repository.PropertyRepository;

@Service
@Transactional
public class PropertyService {

    private final PropertyRepository propertyRepository;

    // ---- Constructor Injection (replaces @RequiredArgsConstructor) ---- //
    public PropertyService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    // ---------------- Service Methods (unchanged) ---------------- //

    public Property createProperty(Property property) {
        return propertyRepository.save(property);
    }

    public Property getPropertyById(Long id) {
        return propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + id));
    }

    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }

    public List<Property> searchProperties(String type, String location, Double minPrice, Double maxPrice) {
        return propertyRepository.searchProperties(type, location, minPrice, maxPrice);
    }

    public Property updateProperty(Long id, Property propertyDetails) {
        Property property = getPropertyById(id);
        property.setTitle(propertyDetails.getTitle());
        property.setDescription(propertyDetails.getDescription());
        property.setPrice(propertyDetails.getPrice());
        property.setLocation(propertyDetails.getLocation());
        property.setSize(propertyDetails.getSize());
        property.setType(propertyDetails.getType());

        if (propertyDetails.getImageUrl() != null && !propertyDetails.getImageUrl().isEmpty()) {
            property.setImageUrl(propertyDetails.getImageUrl());
        }

        return propertyRepository.save(property);
    }

    public void deleteProperty(Long id) {
        Property property = getPropertyById(id);
        propertyRepository.delete(property);
    }

    public long countProperties() {
        return propertyRepository.count();
    }
}
