package com.realestate.management.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "properties")
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Description is required")
    @Column(nullable = false, length = 2000)
    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    @Column(nullable = false)
    private Double price;

    @NotBlank(message = "Location is required")
    @Column(nullable = false)
    private String location;

    @NotNull(message = "Size is required")
    @Positive(message = "Size must be positive")
    @Column(nullable = false)
    private Double size;

    @NotBlank(message = "Type is required")
    @Column(nullable = false)
    private String type; // House, Apartment, Villa, Commercial, Land

    @Column(name = "image_url")
    private String imageUrl;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL)
    private List<Inquiry> inquiries;

    // ---------------- Constructors ---------------- //

    public Property() { }

    public Property(String title, String description, Double price, String location, Double size, String type, String imageUrl) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.location = location;
        this.size = size;
        this.type = type;
        this.imageUrl = imageUrl;
    }

    // ---------------- Getters & Setters ---------------- //

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<Inquiry> getInquiries() {
        return inquiries;
    }

    public void setInquiries(List<Inquiry> inquiries) {
        this.inquiries = inquiries;
    }
}
