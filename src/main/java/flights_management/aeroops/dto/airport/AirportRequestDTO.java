package flights_management.aeroops.dto.airport;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AirportRequestDTO(
        @NotBlank @Pattern(regexp = "^[A-Z]{3}$",
                message = "IATA must be exactly 3 uppercase letters")
        String iataCode,
        @NotBlank(message = "Name cannot be blank")
        String name,
        @NotBlank(message = "City cannot be blank")
        String city,
        @NotBlank(message = "Country cannot be blank")
        String country,
        @NotBlank(message = "Time zone ID cannot be blank")
        String timeZoneId
) { }
