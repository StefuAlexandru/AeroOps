package flights_management.aeroops.dto.flight;

import flights_management.aeroops.enums.SeatClass;

public record SeatRequestDTO(Long flightId, String seatNumber, SeatClass seatClass) { }
