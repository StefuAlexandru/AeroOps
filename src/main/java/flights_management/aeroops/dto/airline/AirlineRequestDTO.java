package flights_management.aeroops.dto.airline;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AirlineRequestDTO(
        @NotBlank(message = "Name cannot be blank")
        String name,
        @NotBlank(message = "IATA code cannot be blank")
        @Size(min = 2, max = 3, message = "IATA/ICAO must have 2-3 letters")
        String iataCode,
        @NotBlank(message = "Country cannot be blank")
        String country
) { }
