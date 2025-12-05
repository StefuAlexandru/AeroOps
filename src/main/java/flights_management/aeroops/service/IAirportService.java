package flights_management.aeroops.service;

import flights_management.aeroops.dto.airport.AirportRequestDTO;
import flights_management.aeroops.dto.airport.AirportResponseDTO;
import java.util.List;

public interface IAirportService {
    AirportResponseDTO createAirport(AirportRequestDTO request);
    List<AirportResponseDTO> getAllAirports();
    AirportResponseDTO updateAirport(Long id, AirportRequestDTO request);
    void deleteAirport(Long id);
}
