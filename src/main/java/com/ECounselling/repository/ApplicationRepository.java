package com.ECounselling.repository;

import com.ECounselling.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Optional<Application> findByStudentName(String studentName);
}
