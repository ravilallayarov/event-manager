package org.example.eventmanager.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "locations")
@Data
public class LocationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private Integer capacity;
    private String description;
}
