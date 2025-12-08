package flights_management.aeroops.service;

import flights_management.aeroops.dto.aircraft.AircraftRequestDTO;
import flights_management.aeroops.dto.aircraft.AircraftResponseDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface IAircraftService {

    AircraftResponseDTO createAircraft(AircraftRequestDTO requestDTO);
    List<AircraftResponseDTO> getAllAircrafts();
    AircraftResponseDTO updateAircraft(Long id, AircraftRequestDTO requestDTO);
    void deleteAircraft(Long id);
}
