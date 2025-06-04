package org.example.eventmanager.mapper;


import org.example.eventmanager.dto.LocationDto;
import org.example.eventmanager.entity.LocationEntity;
import org.example.eventmanager.model.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    Location toLocationFromDto(LocationDto locationDto);
    LocationEntity toEntityFromLocation(Location location);
    Location toLocationFromEntity(LocationEntity locationEntity);
    LocationDto toDtoFromLocation(Location location);
    @Mapping(target = "id", ignore = true) // если id нельзя менять
    void updateEntityFromLocation(Location location, @MappingTarget LocationEntity entity);
}
