package org.example.eventmanager.location.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.eventmanager.location.dto.LocationDto;
import org.example.eventmanager.location.mapper.LocationMapper;
import org.example.eventmanager.location.model.Location;
import org.example.eventmanager.location.service.LocationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;
    private final LocationMapper locationMapper;


    @GetMapping
    public ResponseEntity<List<LocationDto>> findAll() {
        List<LocationDto> locations = locationService.findAll().stream()
                .map(locationMapper::toDtoFromLocation)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(locations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationDto> findById(@PathVariable Long id) {
        LocationDto locationDto = locationMapper.toDtoFromLocation(locationService.findById(id));
        return ResponseEntity.status(HttpStatus.OK).body(locationDto);
    }

    @PostMapping
    public ResponseEntity<LocationDto> create(@RequestBody @Valid LocationDto locationDto) {
        Location savedLocation = locationService.create(locationMapper.toLocationFromDto(locationDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(locationMapper.toDtoFromLocation(savedLocation));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationDto> update(@PathVariable Long id, @RequestBody @Valid LocationDto locationDto) {
        Location updatedLocation = locationService.update(id, locationMapper.toLocationFromDto(locationDto));
        return ResponseEntity.status(HttpStatus.OK).body(locationMapper.toDtoFromLocation(updatedLocation));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<LocationDto> delete(@PathVariable Long id) {
        Location deletedLocation = locationService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(locationMapper.toDtoFromLocation(deletedLocation));
    }
}
