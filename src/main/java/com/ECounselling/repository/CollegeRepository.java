package com.ECounselling.repository;

import com.ECounselling.model.College;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CollegeRepository extends JpaRepository<College, Long> {
    Optional<College> findByCollegeName(String collegeName);
    boolean existsByCollegeName(String collegeName);
    boolean existsByMailId(String mailId);
    Optional<College> findByMailId(String mailId);
}
