package flights_management.aeroops.dto.airline;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AirlineRequestDTO(
        @NotBlank(message = "Name cannot be blank")
        String name,
        @NotBlank @Pattern(regexp = "^[A-Z]{2,3}$",
                message = "IATA/ICAO must be 2–3 uppercase letters")
        String iataCode,
        @NotBlank(message = "Country cannot be blank")
        String country
) { }
