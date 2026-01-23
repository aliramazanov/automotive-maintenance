package com.automotive.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "feature")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "feature_id", unique = true)
    private Integer featureId;

    @Column(length = 255)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(length = 255)
    private String category;

    // Optional reverse mapping
    @ManyToMany(mappedBy = "features", fetch = FetchType.LAZY)
    private List<Car> cars;
}