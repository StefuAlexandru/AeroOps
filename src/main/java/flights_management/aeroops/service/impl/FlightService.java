package flights_management.aeroops.service.impl;

import flights_management.aeroops.dto.flight.FlightRequestDTO;
import flights_management.aeroops.dto.flight.FlightResponseDTO;
import flights_management.aeroops.entity.Airline;
import flights_management.aeroops.entity.Airport;
import flights_management.aeroops.entity.Flight;
import flights_management.aeroops.error.BusinessException;
import flights_management.aeroops.error.ErrorModel;
import flights_management.aeroops.mapper.FlightMapper;
import flights_management.aeroops.repository.AirlineRepository;
import flights_management.aeroops.repository.AirportRepository;
import flights_management.aeroops.repository.FlightRepository;
import flights_management.aeroops.service.IFlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FlightService implements IFlightService {

    private final FlightRepository flightRepository;
    private final AirlineRepository airlineRepository;
    private final AirportRepository airportRepository;
    private final FlightMapper flightMapper;

    @Override
    public FlightResponseDTO createFlight(FlightRequestDTO flightRequestDTO) throws BusinessException {
        List<ErrorModel> errors = new ArrayList<>();
        Airline airline = airlineRepository.findById(flightRequestDTO.airlineId()).orElse(null);
        if (airline == null){
            errors.add(new ErrorModel("AIRLINE_NOT_FOUND", "Airline not found"));
        }
        Airport origin = airportRepository.findById(flightRequestDTO.originAirportId()).orElse(null);
        if (origin == null){
            errors.add(new ErrorModel("ORIGIN_NOT_FOUND", "Origin airport not found"));
        }
        Airport destination = airportRepository.findById(flightRequestDTO.destinationAirportId()).orElse(null);
        if (destination == null){
            errors.add(new ErrorModel("DESTINATION_NOT_FOUND", "Destination airport not found"));
        }

        if(!errors.isEmpty()) throw new BusinessException(errors);

        Flight flight = flightMapper.toEntity(flightRequestDTO,airline,origin,destination);

        flightRepository.save(flight);
        return flightMapper.toResponse(flight);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FlightResponseDTO> getAllFlights() {
        return flightRepository.findAll()
                .stream().map(flightMapper::toResponse).toList();
    }
}
