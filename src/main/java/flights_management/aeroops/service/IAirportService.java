package flights_management.aeroops.service;

import flights_management.aeroops.dto.airport.AirportDTO;
import flights_management.aeroops.dto.airport.AirportRequestDTO;
import flights_management.aeroops.dto.airport.AirportResponseDTO;

import java.util.List;

public interface IAirportService {
    AirportResponseDTO create(AirportRequestDTO req);
    List<AirportResponseDTO> getAll();
}
