package flights_management.aeroops.dto.airline;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AirlineRequestDTO(
        @NotBlank String name,
        @NotBlank @Pattern(regexp = "^[A-Z]{2,3}$",
                message = "IATA/ICAO must be 2–3 uppercase letters")
        String iataCode,
        @NotBlank String country
) {}