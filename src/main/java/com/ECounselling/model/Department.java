package com.ECounselling.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "Department")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long departmentId;

    private String departmentName;
    private Integer noOfSeats;
    private Integer cutoffRank;

    @ManyToOne
    @JoinColumn(name = "college_id", nullable = false)
    @JsonBackReference
    private College college;

}

