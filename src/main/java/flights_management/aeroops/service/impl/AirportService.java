package flights_management.aeroops.service.impl;

import flights_management.aeroops.dto.airport.AirportRequestDTO;
import flights_management.aeroops.dto.airport.AirportResponseDTO;
import flights_management.aeroops.entity.Airport;
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
}
