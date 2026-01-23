package com.automotive.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cardetails")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "car_id", nullable = false, unique = true)
    private Car car;

    @Column(name = "engine_number", nullable = false, length = 20)
    private String engineNumber;

    @Column(name = "registration_code", nullable = false, length = 20, unique = true)
    private String registrationCode;

    @Column(name = "fuel_type", nullable = false, length = 20)
    private String fuelType;

    @Column(name = "engine_capacity", nullable = false, length = 200)
    private String engineCapacity;

    @Column(length = 255)
    private String color;

    @Column(name = "insurance_number", length = 255)
    private String insuranceNumber;
}