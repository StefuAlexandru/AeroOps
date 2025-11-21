package flights_management.aeroops.service;

import flights_management.aeroops.dto.airline.AirlineRequestDTO;
import flights_management.aeroops.dto.airline.AirlineResponseDTO;

import java.util.List;

public interface IAirlineService {
    AirlineResponseDTO create(AirlineRequestDTO req);
    List<AirlineResponseDTO> getAll();
}
