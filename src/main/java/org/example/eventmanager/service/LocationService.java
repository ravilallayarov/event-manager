package org.example.eventmanager.service;

import org.example.eventmanager.model.Location;

import java.util.List;

public interface LocationService {
    List<Location> findAll();
    Location findById(Long id);
    Location create(Location location);
    Location update(Long id, Location location);
    Location delete(Long id);
}
