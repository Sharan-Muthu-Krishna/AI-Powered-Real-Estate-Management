package com.realestate.management.util;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.realestate.management.entity.Property;
import com.realestate.management.entity.User;
import com.realestate.management.repository.PropertyRepository;
import com.realestate.management.repository.UserRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;

    // ---- Constructor Injection (replaces @RequiredArgsConstructor) ---- //
    public DataSeeder(UserRepository userRepository, PropertyRepository propertyRepository) {
        this.userRepository = userRepository;
        this.propertyRepository = propertyRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            seedUsers();
        }

        if (propertyRepository.count() == 0) {
            seedProperties();
        }
    }

    private void seedUsers() {
        User admin = new User("Admin User", "admin@realestate.com", "admin123", "ADMIN");

        User agent1 = new User("John Agent", "agent1@realestate.com", "agent123", "AGENT");
        User agent2 = new User("Sarah Agent", "agent2@realestate.com", "agent123", "AGENT");

        User customer1 = new User("Alice Customer", "alice@example.com", "customer123", "CUSTOMER");
        User customer2 = new User("Bob Customer", "bob@example.com", "customer123", "CUSTOMER");
        User customer3 = new User("Charlie Customer", "charlie@example.com", "customer123", "CUSTOMER");
        User customer4 = new User("Diana Customer", "diana@example.com", "customer123", "CUSTOMER");
        User customer5 = new User("Eve Customer", "eve@example.com", "customer123", "CUSTOMER");

        userRepository.saveAll(Arrays.asList(
                admin, agent1, agent2,
                customer1, customer2, customer3, customer4, customer5
        ));

        System.out.println("✓ Users seeded successfully!");
    }

    private void seedProperties() {
        List<Property> properties = Arrays.asList(
                createProperty("Luxury Villa in Beverly Hills",
                        "Stunning 5-bedroom villa with panoramic city views, infinity pool, and modern amenities.",
                        2500000.0, "Beverly Hills, CA", 4500.0, "Villa",
                        "https://images.unsplash.com/photo-1613490493576-7fde63acd811?w=800"),

                createProperty("Modern Downtown Apartment",
                        "Contemporary 2-bedroom apartment in the heart of downtown with gym and rooftop access.",
                        450000.0, "New York, NY", 1200.0, "Apartment",
                        "https://images.unsplash.com/photo-1545324418-cc1a3fa10c00?w=800"),

                createProperty("Spacious Family House",
                        "Beautiful 4-bedroom house with large backyard, perfect for families.",
                        675000.0, "Austin, TX", 3200.0, "House",
                        "https://images.unsplash.com/photo-1568605114967-8130f3a36994?w=800"),

                createProperty("Beachfront Condo",
                        "Luxurious 3-bedroom condo with direct beach access and ocean views.",
                        890000.0, "Miami, FL", 1800.0, "Apartment",
                        "https://images.unsplash.com/photo-1512917774080-9991f1c4c750?w=800"),

                createProperty("Commercial Office Space",
                        "Prime commercial property in business district, 10,000 sq ft with parking.",
                        1200000.0, "San Francisco, CA", 10000.0, "Commercial",
                        "https://images.unsplash.com/photo-1486406146926-c627a92ad1ab?w=800"),

                createProperty("Suburban Dream Home",
                        "Charming 3-bedroom home in quiet suburban neighborhood with excellent schools.",
                        385000.0, "Seattle, WA", 2400.0, "House",
                        "https://images.unsplash.com/photo-1572120360610-d971b9d7767c?w=800"),

                createProperty("Penthouse Suite",
                        "Exclusive penthouse with 360-degree city views, 4 bedrooms, and private elevator.",
                        3500000.0, "Chicago, IL", 5200.0, "Apartment",
                        "https://images.unsplash.com/photo-1600596542815-ffad4c1539a9?w=800"),

                createProperty("Mountain Retreat",
                        "Secluded mountain cabin with 3 bedrooms, perfect for weekend getaways.",
                        525000.0, "Denver, CO", 2100.0, "House",
                        "https://images.unsplash.com/photo-1542718610-a1d656d1884c?w=800"),

                createProperty("Investment Land Plot",
                        "Prime development land, 2 acres with all utilities ready for construction.",
                        350000.0, "Phoenix, AZ", 87120.0, "Land",
                        "https://images.unsplash.com/photo-1500382017468-9049fed747ef?w=800"),

                createProperty("Historic Townhouse",
                        "Beautifully restored 3-story townhouse with original features and modern updates.",
                        795000.0, "Boston, MA", 2800.0, "House",
                        "https://images.unsplash.com/photo-1580587771525-78b9dba3b914?w=800")
        );

        propertyRepository.saveAll(properties);
        System.out.println("✓ Properties seeded successfully!");
    }

    private Property createProperty(String title, String description, Double price,
                                    String location, Double size, String type, String imageUrl) {
        Property property = new Property();
        property.setTitle(title);
        property.setDescription(description);
        property.setPrice(price);
        property.setLocation(location);
        property.setSize(size);
        property.setType(type);
        property.setImageUrl(imageUrl);
        return property;
    }
}
