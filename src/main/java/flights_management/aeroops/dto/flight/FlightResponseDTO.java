package flights_management.aeroops.dto.flight;

import flights_management.aeroops.dto.airline.AirlineDTO;
import flights_management.aeroops.dto.airport.AirportDTO;
import flights_management.aeroops.enums.Status;

import java.time.ZonedDateTime;

// Pentru detalii complete in controller
public record FlightResponseDTO(
        Long id,
        AirlineDTO airline,
        AirportDTO originAirport,
        AirportDTO destinationAirport,
        String flightNumber,
        ZonedDateTime scheduledDeparture,
        ZonedDateTime scheduledArrival,
        Status status
)
{ }
