package org.example.eventmanager.location.mapper;


import org.example.eventmanager.location.dto.LocationDto;
import org.example.eventmanager.location.entity.LocationEntity;
import org.example.eventmanager.location.model.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    Location toLocationFromDto(LocationDto locationDto);

    LocationEntity toEntityFromLocation(Location location);

    Location toLocationFromEntity(LocationEntity locationEntity);

    LocationDto toDtoFromLocation(Location location);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromLocation(Location location, @MappingTarget LocationEntity entity);
}
