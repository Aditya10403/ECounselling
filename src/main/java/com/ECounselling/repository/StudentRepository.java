package com.ECounselling.repository;

import com.ECounselling.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByMailId(String mailId);
    boolean existsByMailId(String mailId);
    Optional<Student> findByStudentName(String studentName);
}
