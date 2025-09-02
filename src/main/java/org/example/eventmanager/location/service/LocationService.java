package org.example.eventmanager.location.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eventmanager.location.entity.LocationEntity;
import org.example.eventmanager.exception.InvalidCapacityException;
import org.example.eventmanager.exception.NotFoundException;
import org.example.eventmanager.location.mapper.LocationMapper;
import org.example.eventmanager.location.model.Location;
import org.example.eventmanager.location.repository.LocationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationService {
    private final LocationRepository locationRepository;
    private final LocationMapper locationMapper;


    @Transactional(readOnly = true)
    public List<Location> findAll() {
        log.info("trying to find all locations");
        List<Location> locations = locationRepository.findAll().stream()
                .map(locationMapper::toLocationFromEntity)
                .toList();
        log.info("all locations successfully found, size: {}", locations.size());
        return locations;
    }


    @Transactional(readOnly = true)
    public Location findById(Long id) {
        return locationMapper.toLocationFromEntity(findEntityById(id));
    }

    @Transactional
    public Location create(Location location) {
        log.info("trying to create location");
        LocationEntity entityToSave = locationMapper.toEntityFromLocation(location);
        LocationEntity savedEntity = locationRepository.save(entityToSave);
        log.info("location successfully created, id: {}", savedEntity.getId());
        return locationMapper.toLocationFromEntity(savedEntity);
    }

    @Transactional
    public Location update(Long id, Location location) {
        log.info("trying to update location by id: {}", id);
        LocationEntity entity = findEntityById(id);
        if (location.getCapacity() < entity.getCapacity()) {
            throw new InvalidCapacityException("Capacity must not be less than " + entity.getCapacity());
        }
        locationMapper.updateEntityFromLocation(location, entity);
        log.info("location by id: {} successfully updated", id);
        return locationMapper.toLocationFromEntity(entity);
    }

    @Transactional
    public Location delete(Long id) {
        log.info("trying to delete location by id: {}", id);
        LocationEntity entity = findEntityById(id);
        locationRepository.delete(entity);
        log.info("location by id: {} successfully deleted", id);
        return locationMapper.toLocationFromEntity(entity);
    }

    public LocationEntity findEntityById(Long id) {
        log.info("trying to find location by id: {}", id);
        LocationEntity entity = locationRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Location by id: {} not found", id);
                    return new NotFoundException("Location by id: " + id + " not found");
                });
        log.info("location by id: {} successfully found", id);
        return entity;
    }
}

