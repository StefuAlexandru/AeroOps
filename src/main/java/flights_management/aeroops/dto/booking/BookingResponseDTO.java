package flights_management.aeroops.dto.booking;

import flights_management.aeroops.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.Instant;

public record BookingResponseDTO(Long id, String code, Long flightId, Long passengerId, BookingStatus status, BigDecimal priceTotal, Instant createdAt) {
}
