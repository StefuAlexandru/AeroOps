package flights_management.aeroops.dto.airport;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AirportRequestDTO(
        @NotBlank @Pattern(regexp = "^[A-Z]{3}$",
                message = "IATA must be exactly 3 uppercase letters")
        String iataCode,
        @NotBlank String name,
        @NotBlank String city,
        @NotBlank String country,
        @NotBlank String timeZoneId
) {}