package flights_management.aeroops.service.impl;

import flights_management.aeroops.dto.flight.FlightRequestDTO;
import flights_management.aeroops.dto.flight.FlightResponseDTO;
import flights_management.aeroops.entity.Aircraft;
import flights_management.aeroops.entity.Airline;
import flights_management.aeroops.entity.Airport;
import flights_management.aeroops.entity.Flight;
import flights_management.aeroops.enums.ErrorCode;
import flights_management.aeroops.error.BusinessException;
import flights_management.aeroops.error.ErrorModel;
import flights_management.aeroops.mapper.FlightMapper;
import flights_management.aeroops.repository.AircraftRepository;
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
    private final AircraftRepository aircraftRepository;
    private final FlightMapper flightMapper;

    @Override
    public FlightResponseDTO createFlight(FlightRequestDTO flightRequestDTO) {
        ValidatedFlightData validated = validateAndFetch(flightRequestDTO);

        Flight flight = flightMapper.toEntity(
                flightRequestDTO,
                validated.airline(),
                validated.origin(),
                validated.destination(),
                validated.aircraft()
        );

        flightRepository.save(flight);
        return flightMapper.toResponse(flight);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FlightResponseDTO> getAllFlights() {
        return flightRepository.findAll()
                .stream().map(flightMapper::toResponse).toList();
    }

    @Override
    public FlightResponseDTO updateFlight(Long id, FlightRequestDTO request) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        List.of(new ErrorModel(ErrorCode.FLIGHT_NOT_FOUND))
                ));

        ValidatedFlightData validated = validateAndFetch(request);

        flight.setAirline(validated.airline());
        flight.setOriginAirport(validated.origin());
        flight.setDestinationAirport(validated.destination());
        flight.setAircraft(validated.aircraft());
        flight.setFlightNumber(request.flightNumber());
        flight.setScheduledDeparture(request.scheduledDeparture().toInstant());
        flight.setScheduledArrival(request.scheduledArrival().toInstant());

        Flight updatedFlight = flightRepository.save(flight);
        return flightMapper.toResponse(updatedFlight);
    }

    @Override
    public void deleteFlight(Long id) {
        Flight flight = flightRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        List.of(new ErrorModel(ErrorCode.FLIGHT_NOT_FOUND))
                ));
        flightRepository.delete(flight);
    }

    private record ValidatedFlightData(
            Airline airline,
            Airport origin,
            Airport destination,
            Aircraft aircraft
    ) {
    }

    private ValidatedFlightData validateAndFetch(FlightRequestDTO request) {
        List<ErrorModel> errors = new ArrayList<>();

        Airline airline = airlineRepository.findById(request.airlineId()).orElse(null);
        if (airline == null) {
            errors.add(new ErrorModel(ErrorCode.AIRLINE_NOT_FOUND));
        }

        Airport origin = airportRepository.findById(request.originAirportId()).orElse(null);
        if (origin == null) {
            errors.add(new ErrorModel(ErrorCode.ORIGIN_NOT_FOUND));
        }

        Airport destination = airportRepository.findById(request.destinationAirportId()).orElse(null);
        if (destination == null) {
            errors.add(new ErrorModel(ErrorCode.DESTINATION_NOT_FOUND));
        }

        Aircraft aircraft = aircraftRepository.findById(request.aircraftId()).orElse(null);
        if (aircraft == null) {
            errors.add(new ErrorModel(ErrorCode.AIRCRAFT_NOT_FOUND));
        }

        if (!errors.isEmpty()) {
            throw new BusinessException(errors);
        }

        return new ValidatedFlightData(airline, origin, destination, aircraft);
    }
}