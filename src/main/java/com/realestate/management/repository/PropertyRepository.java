// PropertyRepository.java - src/main/java/com/realestate/management/repository/PropertyRepository.java
package com.realestate.management.repository;

import com.realestate.management.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> findByType(String type);
    List<Property> findByLocation(String location);
    
    @Query("SELECT p FROM Property p WHERE " +
           "(:type IS NULL OR p.type = :type) AND " +
           "(:location IS NULL OR p.location LIKE %:location%) AND " +
           "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR p.price <= :maxPrice)")
    List<Property> searchProperties(
        @Param("type") String type,
        @Param("location") String location,
        @Param("minPrice") Double minPrice,
        @Param("maxPrice") Double maxPrice
    );
}
