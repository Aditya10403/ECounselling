package com.ECounselling.repository;

import com.ECounselling.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query("SELECT d FROM Department d WHERE d.cutoffRank >= :erank")
    List<Department> findDepartmentsByCutoffRank(@Param("erank") Integer erank);
}
