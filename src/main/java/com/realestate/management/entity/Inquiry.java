package com.realestate.management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name = "inquiries")
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @NotBlank(message = "Message is required")
    @Column(nullable = false, length = 1000)
    private String message;

    @Column(nullable = false)
    private String status = "PENDING"; // PENDING, CONTACTED, CLOSED

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public Inquiry() { }

    public Inquiry(User user, Property property, String message, String status, LocalDateTime timestamp) {
        this.user = user;
        this.property = property;
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
    }

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }

    // ---------------- Getters & Setters ---------------- //

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
