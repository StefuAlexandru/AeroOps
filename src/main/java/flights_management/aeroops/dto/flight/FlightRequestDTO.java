package flights_management.aeroops.dto.flight;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.ZonedDateTime;

// Pentru input (creare/editate)
public record FlightRequestDTO(
        @NotNull(message = "Airline ID cannot be null")
        Long airlineId,
        @NotNull(message = "Origin airport ID cannot be null")
        Long originAirportId,
        @NotNull(message = "Destination airport ID cannot be null")
        Long destinationAirportId,
        @NotNull(message = "Aircraft ID cannot be null")
        Long aircraftId,
        @NotBlank(message = "Flight number cannot be empty/null")
        String flightNumber,
        @NotNull(message = "Scheduled departure cannot be null")
        @Future(message = "Scheduled departure must be in the future")
        ZonedDateTime scheduledDeparture,
        @NotNull(message = "Scheduled arrival cannot be null")
        @Future(message = "Scheduled arrival must be in the future")
        ZonedDateTime scheduledArrival
) {
}
