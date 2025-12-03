package flights_management.aeroops.dto.airline;

import java.time.Instant;

public record AirlineResponseDTO(
        Long id,
        String name,
        String iataCode,
        String country,
        Instant createdAt,
        Instant updatedAt
) { }
