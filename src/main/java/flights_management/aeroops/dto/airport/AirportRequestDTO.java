package flights_management.aeroops.dto.airport;

import jakarta.validation.constraints.NotBlank;

public record AirportRequestDTO(
        @NotBlank(message = "IATA code cannot be blank")
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
