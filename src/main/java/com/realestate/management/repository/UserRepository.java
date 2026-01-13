// UserRepository.java - src/main/java/com/realestate/management/repository/UserRepository.java
package com.realestate.management.repository;

import com.realestate.management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}