package flights_management.aeroops.dto.aircraft;

import java.time.Instant;

public record AircraftResponseDTO(
        Long id,
        String registration,
        String type,
        String manufacturer,
        Integer seats,
        Instant createdAt,
        Instant updatedAt
) {
}
