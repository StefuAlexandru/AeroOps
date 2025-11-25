package flights_management.aeroops.dto.seat;

import flights_management.aeroops.enums.SeatClass;

//de la Flight: flightNumber
public record SeatResponseDTO(
        Long id,
        String seatNumber,
        SeatClass seatClass,
        Boolean isAvailable,
        String flightNumber
) { }
