package org.example.eventmanager.location.model;


import lombok.Data;

@Data
public class Location {
    private Long id;
    private String name;
    private String address;
    private Integer capacity;
    private String description;
}
