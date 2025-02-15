package com.ECounselling.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "applications")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String studentName;
    private Integer erank;

    @ElementCollection
    @CollectionTable(name = "first_preference", joinColumns = @JoinColumn(name = "application_id"))
    @MapKeyColumn(name = "preference_type")
    @Column(name = "value")
    private Map<String, String> firstPreference;

    @ElementCollection
    @CollectionTable(name = "second_preference", joinColumns = @JoinColumn(name = "application_id"))
    @MapKeyColumn(name = "preference_type")
    @Column(name = "value")
    private Map<String, String> secondPreference;

    @ElementCollection
    @CollectionTable(name = "third_preference", joinColumns = @JoinColumn(name = "application_id"))
    @MapKeyColumn(name = "preference_type")
    @Column(name = "value")
    private Map<String, String> thirdPreference;

}


