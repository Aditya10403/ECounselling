package com.ECounselling.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "counselling_status")
public class CounsellingStatus {
    @Id
    private Long id = 1L;
    private boolean counsellingStarted;
}