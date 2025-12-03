package flights_management.aeroops.dto.airport;

import java.time.Instant;

public record AirportResponseDTO(
        Long id,
        String iataCode,
        String name,
        String city,
        String country,
        String timeZoneId,
        Instant createdAt,
        Instant updatedAt
) { }
