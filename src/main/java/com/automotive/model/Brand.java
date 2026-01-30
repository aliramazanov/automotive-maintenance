package com.automotive.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "brand")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@NamedEntityGraph(
        name = "Brand.models",
        attributeNodes = @NamedAttributeNode("models")
)
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 255)
    private String name;

    @Column(length = 255)
    private String country;

    @Column(name = "founded_year")
    private Integer foundedYear;

    @Column(name = "trigger_column", length = 200)
    private String triggerColumn;

    @OneToMany(mappedBy = "brand", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, orphanRemoval = true)
    private List<Model> models;
}
