package flights_management.aeroops.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    SEAT_NOT_FOUND("Seat not found"),
    FLIGHT_NOT_FOUND("Flight not found"),
    TICKET_NOT_FOUND("Ticket not found"),
    BOOKING_NOT_FOUND("Booking not found");
    private final String message;
}
