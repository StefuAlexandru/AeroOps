package flights_management.aeroops.service;

import flights_management.aeroops.dto.airline.AirlineRequestDTO;
import flights_management.aeroops.dto.airline.AirlineResponseDTO;

import java.util.List;

public interface IAirlineService {
    AirlineResponseDTO createAirline(AirlineRequestDTO request);
    List<AirlineResponseDTO> getAllAirlines();
    AirlineResponseDTO updateAirline(Long id, AirlineRequestDTO request);
    void deleteAirline(Long id);
}
