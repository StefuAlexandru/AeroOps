package flights_management.aeroops.dto.airport;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AirportDTO(
        Long id, String iataCode, String name
) {}
