package flights_management.aeroops.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    SEAT_NOT_FOUND("Seat not found"),
    FLIGHT_NOT_FOUND("Flight not found"),
    TICKET_NOT_FOUND("Ticket not found"),
    BOOKING_NOT_FOUND("Booking not found"),
    AIRLINE_NOT_FOUND("Airline not found"),
    AIRPORT_NOT_FOUND("Airport not found"),
    AIRLINE_IATA_EXISTS("Airline with this IATA already exists");
    private final String message;
}
