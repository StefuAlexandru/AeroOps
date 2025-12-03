package flights_management.aeroops.dto.passenger;

import java.time.Instant;

public record PassengerResponseDTO(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        Instant createdAt,
        Instant updatedAt
) {
}
