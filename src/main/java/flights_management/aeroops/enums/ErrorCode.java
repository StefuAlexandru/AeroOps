package flights_management.aeroops.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    SEAT_NOT_FOUND(HttpStatus.NOT_FOUND, "Seat not found"),
    FLIGHT_NOT_FOUND(HttpStatus.NOT_FOUND, "Flight not found"),
    TICKET_NOT_FOUND(HttpStatus.NOT_FOUND, "Ticket not found"),
    BOOKING_NOT_FOUND(HttpStatus.NOT_FOUND, "Booking not found"),
    AIRLINE_NOT_FOUND(HttpStatus.NOT_FOUND, "Airline not found"),
    AIRPORT_NOT_FOUND(HttpStatus.NOT_FOUND, "Airport not found"),
    PASSENGER_NOT_FOUND(HttpStatus.NOT_FOUND, "Passenger not found"),
    ORIGIN_NOT_FOUND(HttpStatus.NOT_FOUND, "Origin not found"),
    DESTINATION_NOT_FOUND(HttpStatus.NOT_FOUND, "Destination airport not found"),
    AIRCRAFT_NOT_FOUND(HttpStatus.NOT_FOUND, "Aircraft not found"),

    AIRLINE_IATA_EXISTS(HttpStatus.CONFLICT, "Airline with this IATA already exists"),
    AIRCRAFT_REGISTRATION_EXISTS(HttpStatus.CONFLICT, "Aircraft registration already exists"),
    SEAT_ALREADY_EXISTS_FOR_THIS_FLIGHT(HttpStatus.CONFLICT, "Seat number already exists for this flight"),
    AIRCRAFT_CAPACITY_REACHED(HttpStatus.CONFLICT, "Aircraft maximum capacity has been reached"),
    SEAT_HAS_TICKETS_CANNOT_MOVE_TO_OTHER_FLIGHT(HttpStatus.CONFLICT,
            "Seat has tickets and cannot be moved to another flight"),
    SEAT_HAS_TICKETS_CANNOT_DELETE(HttpStatus.CONFLICT,
            "Seat has tickets and cannot be deleted"),

    FLIGHT_CANCELLED(HttpStatus.BAD_REQUEST, "Flight is cancelled"),
    FLIGHT_DEPARTED(HttpStatus.BAD_REQUEST, "Flight has already departed");

    private final HttpStatus status;
    private final String message;
}
