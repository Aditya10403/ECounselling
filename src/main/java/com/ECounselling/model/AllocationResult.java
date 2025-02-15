package com.ECounselling.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "allocation_results")
public class AllocationResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String studentName;
    private String departmentName;
    private String collegeName;

    public AllocationResult(String studentName, String departmentName, String collegeName) {
        this.studentName = studentName;
        this.departmentName = departmentName;
        this.collegeName = collegeName;
    }

}
