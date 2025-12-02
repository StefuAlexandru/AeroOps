package flights_management.aeroops.service;

import flights_management.aeroops.dto.aircraft.AircraftRequestDTO;
import flights_management.aeroops.dto.aircraft.AircraftResponseDTO;

import java.util.List;

public interface IAircraftService {

    AircraftResponseDTO createAircraft(AircraftRequestDTO requestDTO);
    List<AircraftResponseDTO> listAircraft();
}
