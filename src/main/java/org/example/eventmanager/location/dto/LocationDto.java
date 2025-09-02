package org.example.eventmanager.location.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LocationDto {
    private Long id;
    @NotBlank(message = "location name cannot be blank")
    private String name;
    @NotBlank(message = "location address cannot be blank")
    private String address;
    @NotNull(message = "location capacity cannot be null")
    @Min(value = 5, message = "capacity must be at least 5")
    private Integer capacity;
    private String description;
}
