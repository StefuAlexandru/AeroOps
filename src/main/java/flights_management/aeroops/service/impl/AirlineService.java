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
@Service @RequiredArgsConstructor @Transactional
public class AirlineService implements IAirlineService {
    private final AirlineRepository repo;
    private final AirlineMapper mapper;

    @Override
    public AirlineResponseDTO create(AirlineRequestDTO req) {
        List<ErrorModel> errors = new ArrayList<>();
        if (repo.existsByIataCodeIgnoreCase(req.iataCode())) {
            errors.add(new ErrorModel("AIRLINE_IATA_EXISTS","Airline with this IATA already exists"));
        }
        if (!errors.isEmpty()) throw new BusinessException(errors);

        Airline entity = mapper.toEntity(req);
        repo.save(entity);
        return mapper.toResponse(entity);
    }

    @Override @Transactional(readOnly = true)
    public List<AirlineResponseDTO> getAll() {
        return repo.findAll().stream().map(mapper::toResponse).toList();
    }
}