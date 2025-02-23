package com.ECounselling.repository;

import com.ECounselling.model.AllocationResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AllocationResultRepository extends JpaRepository<AllocationResult, Long> {
    Optional<AllocationResult> findByStudentName(String studentName);
    List<AllocationResult> findByCollegeName(String collegeName);
}
