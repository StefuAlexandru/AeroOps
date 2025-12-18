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
    AIRCRAFT_NOT_FOUND(HttpStatus.NOT_FOUND, "Aircraft not found"),
    PASSENGER_NOT_FOUND(HttpStatus.NOT_FOUND, "Passenger not found"),
    ORIGIN_NOT_FOUND(HttpStatus.NOT_FOUND, "Origin not found"),
    DESTINATION_NOT_FOUND(HttpStatus.NOT_FOUND, "Destination not found"),

    AIRLINE_IATA_EXISTS(HttpStatus.CONFLICT, "Airline with this IATA already exists"),
    SEAT_ALREADY_EXISTS_FOR_THIS_FLIGHT(HttpStatus.CONFLICT, "Seat number already exists for this flight"),
    AIRCRAFT_CAPACITY_REACHED(HttpStatus.CONFLICT, "Aircraft maximum capacity has been reached"),

    FLIGHT_CANCELLED(HttpStatus.CONFLICT, "Flight is cancelled"),
    FLIGHT_DEPARTED(HttpStatus.CONFLICT, "Flight has already departed"),

    SEAT_HAS_TICKETS_CANNOT_MOVE_TO_OTHER_FLIGHT(HttpStatus.CONFLICT, "Seat has tickets and cannot be moved to another flight"),
    SEAT_HAS_TICKETS_CANNOT_DELETE(HttpStatus.CONFLICT, "Seat has tickets and cannot be deleted"),

    SEAT_NOT_IN_BOOKING_FLIGHT(HttpStatus.CONFLICT, "Seat is not on the same flight" ),
    BOOKING_NOT_ACTIVE(HttpStatus.CONFLICT, "Booking is canceled" ),
    SEAT_NOT_AVAILABLE(HttpStatus.CONFLICT, "Seat is not available"),
    SEAT_ALREADY_ALLOCATED(HttpStatus.CONFLICT, "Seat is already allocated to another ticket"),

    INVALID_TICKET_PRICE(HttpStatus.BAD_REQUEST, "Invalid ticket price, it should be not null and greater than zero" ),
    AIRCRAFT_REGISTRATION_EXISTS(HttpStatus.BAD_REQUEST, "Aircraft registration exists");

    private final HttpStatus status;
    private final String message;
}

