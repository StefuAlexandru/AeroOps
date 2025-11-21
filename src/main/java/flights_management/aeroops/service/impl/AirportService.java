package flights_management.aeroops.service.impl;

import flights_management.aeroops.dto.airport.AirportRequestDTO;
import flights_management.aeroops.dto.airport.AirportResponseDTO;
import flights_management.aeroops.entity.Airport;
import flights_management.aeroops.error.BusinessException;
import flights_management.aeroops.error.ErrorModel;
import flights_management.aeroops.mapper.AirportMapper;
import flights_management.aeroops.repository.AirportRepository;
import flights_management.aeroops.service.IAirportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AirportService implements IAirportService {

    private final AirportRepository repo;
    private final AirportMapper mapper;

    @Override
    public AirportResponseDTO create(AirportRequestDTO req) {
        List<ErrorModel> errors = new ArrayList<>();

        if (repo.existsByIataCodeIgnoreCase(req.iataCode())) {
            errors.add(new ErrorModel("AIRPORT_IATA_EXISTS", "Airport with this IATA already exists"));
        }

        if (!errors.isEmpty()) {
            throw new BusinessException(errors);
        }

        Airport entity = mapper.toEntity(req);
        repo.save(entity);
        return mapper.toResponse(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AirportResponseDTO> getAll() {
        return repo.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
}