package flights_management.aeroops.dto.booking;

import flights_management.aeroops.enums.BookingStatus;

import java.math.BigDecimal;

public record BookingRequestDTO(Long flightId, Long passengerId, BigDecimal priceTotal) {
}
