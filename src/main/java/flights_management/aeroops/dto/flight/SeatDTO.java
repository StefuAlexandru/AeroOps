package flights_management.aeroops.dto.flight;

import flights_management.aeroops.enums.SeatClass;

public record SeatDTO(Long id, Long flightId, String seatNumber, SeatClass seatClass, Boolean isAvailable) {
}
