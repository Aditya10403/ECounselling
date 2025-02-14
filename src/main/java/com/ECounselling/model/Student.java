package com.ECounselling.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long studentId;

    private String studentName;
    private String contactNumber;
    private String address;
    private String tenthboard;
    private String twelfthboard;
    private String schoolName;
    private String mailId;
    private Double tenthMarks;
    private Double twelveMarks;
    private String img;
    private Integer erank;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role = Role.STUDENT;

}
