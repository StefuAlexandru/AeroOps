package flights_management.aeroops.dto.airline;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AirlineDTO(
        Long id, String name, String iataCode
) {}
