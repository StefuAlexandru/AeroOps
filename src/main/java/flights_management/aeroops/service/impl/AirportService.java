package flights_management.aeroops.service.impl;

import flights_management.aeroops.dto.airport.AirportRequestDTO;
import flights_management.aeroops.dto.airport.AirportResponseDTO;
import flights_management.aeroops.entity.Airport;
import flights_management.aeroops.enums.ErrorCode;
import flights_management.aeroops.error.BusinessException;
import flights_management.aeroops.error.ErrorModel;
import flights_management.aeroops.mapper.AirportMapper;
import flights_management.aeroops.repository.AirportRepository;
import flights_management.aeroops.service.IAirportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AirportService implements IAirportService {

    private final AirportRepository airportRepository;
    private final AirportMapper airportMapper;

    @Override
    public AirportResponseDTO createAirport(AirportRequestDTO request) {
        Airport airport = airportMapper.toEntity(request);
        airportRepository.save(airport);
        return airportMapper.toResponse(airport);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AirportResponseDTO> getAllAirports() {
        return airportRepository.findAll()
                .stream()
                .map(airportMapper::toResponse)
                .toList();
    }

    @Override
    public AirportResponseDTO updateAirport(Long id, AirportRequestDTO request) {
        Airport airport = getAirport(id);

        airport.setIataCode(request.iataCode());
        airport.setName(request.name());
        airport.setCity(request.city());
        airport.setCountry(request.country());
        airport.setTimeZoneId(request.timeZoneId());

        Airport updated = airportRepository.save(airport);

        return airportMapper.toResponse(updated);
    }

    @Override
    public void deleteAirport(Long id) {
        Airport airport = getAirport(id);
        airportRepository.delete(airport);
    }

    private Airport getAirport(Long id) {
        return airportRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        List.of(new ErrorModel(ErrorCode.AIRPORT_NOT_FOUND))
                ));
    }
}
