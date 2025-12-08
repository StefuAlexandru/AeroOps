package flights_management.aeroops.service;

import flights_management.aeroops.dto.flight.FlightRequestDTO;
import flights_management.aeroops.dto.flight.FlightResponseDTO;

import java.util.List;

public interface IFlightService {
    FlightResponseDTO createFlight(FlightRequestDTO flightRequestDTO);
    List<FlightResponseDTO> getAllFlights();
    FlightResponseDTO updateFlight(Long id, FlightRequestDTO flightRequestDTO);
    void deleteFlight(Long id);
}
