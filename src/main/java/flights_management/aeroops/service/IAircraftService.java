package flights_management.aeroops.service;

import flights_management.aeroops.dto.airport.AircraftRequest;
import flights_management.aeroops.dto.airport.AircraftResponse;

import java.util.List;

public interface IAircraftService {
    /**
     * @param request DTO containing aircraft details
     * @return AircraftResponse containing persisted data
     */
    AircraftResponse createAircraft(AircraftRequest request);

    /**
     * @return list of aircraft responses
     */
    List<AircraftResponse> listAircraft();
}
