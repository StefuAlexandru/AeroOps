package flights_management.aeroops.dto.seat;

import flights_management.aeroops.enums.SeatClass;

import java.time.Instant;

//de la Flight: flightNumber
public record SeatResponseDTO(
        Long id,
        String seatNumber,
        SeatClass seatClass,
        Boolean isAvailable,
        String flightNumber,
        Instant createdAt,
        Instant updatedAt
) { }
