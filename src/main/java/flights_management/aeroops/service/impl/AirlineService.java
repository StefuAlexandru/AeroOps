package flights_management.aeroops.service.impl;

import flights_management.aeroops.dto.airline.AirlineRequestDTO;
import flights_management.aeroops.dto.airline.AirlineResponseDTO;
import flights_management.aeroops.entity.Airline;
import flights_management.aeroops.error.BusinessException;
import flights_management.aeroops.error.ErrorModel;
import flights_management.aeroops.mapper.AirlineMapper;
import flights_management.aeroops.repository.AirlineRepository;
import flights_management.aeroops.service.IAirlineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AirlineService implements IAirlineService {

    private final AirlineRepository airlineRepository;
    private final AirlineMapper airlineMapper;

    @Override
    public AirlineResponseDTO createAirline(AirlineRequestDTO request) {
        List<ErrorModel> errors = new ArrayList<>();
        if (airlineRepository.existsByIataCodeIgnoreCase(request.iataCode())) {
            errors.add(new ErrorModel("AIRLINE_IATA_EXISTS", "Airline with this IATA already exists"));
        }
        if (!errors.isEmpty()) throw new BusinessException(errors);

        Airline airline = airlineMapper.toEntity(request);
        airlineRepository.save(airline);
        return airlineMapper.toResponse(airline);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AirlineResponseDTO> getAllAirlines() {
        return airlineRepository.findAll().stream()
                .map(airlineMapper::toResponse)
                .toList();
    }
}
