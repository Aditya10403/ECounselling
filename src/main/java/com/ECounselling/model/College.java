package com.ECounselling.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "College")
public class College {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long collegeId;

    @Column(unique = true, nullable = false)
    private String collegeName;

    private String mailId;
    private String password;
    private String address;
    private String contactInfo;
    private Integer nirfRank;
    private String logo;
    private Boolean status; // isBlocked

    @Enumerated(EnumType.STRING)
    private Role role = Role.COLLEGE;

    @OneToMany(mappedBy = "college", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Department> departments;

}
