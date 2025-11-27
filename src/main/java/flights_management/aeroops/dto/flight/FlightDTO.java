package flights_management.aeroops.dto.flight;

import flights_management.aeroops.enums.Status;

import java.time.ZonedDateTime;


// Pentru referinte in alte DTO-uri

public record FlightDTO(
        Long id,
        String flightNumber,
        String airlineName,
        String originIata,
        String destinationIata,
        String aircraftRegistration,
        ZonedDateTime scheduledDeparture,
        ZonedDateTime scheduledArrival,
        Status status
) { }