package flights_management.aeroops.service.impl;


import flights_management.aeroops.dto.airport.AircraftRequest;
import flights_management.aeroops.dto.airport.AircraftResponse;
import flights_management.aeroops.entity.Aircraft;
import flights_management.aeroops.repository.AircraftRepository;
import flights_management.aeroops.service.IAircraftService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class AircraftService implements IAircraftService {

    private final AircraftRepository repo;

    public AircraftService(AircraftRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional
    public AircraftResponse createAircraft(AircraftRequest request) {
        if (repo.existsByRegistration(request.registration())) {
            throw new IllegalArgumentException("Aircraft registration already exists: " + request.registration());
        }

        Aircraft saved = repo.save(Aircraft.builder()
                .registration(request.registration().toUpperCase())
                .type(request.type())
                .manufacturer(request.manufacturer())
                .seats(request.seats())
                .build());

        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AircraftResponse> listAircraft() {
        return repo.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    private AircraftResponse mapToResponse(Aircraft a) {
        return new AircraftResponse(
                a.getId(),
                a.getRegistration(),
                a.getType(),
                a.getManufacturer(),
                a.getSeats()
        );
    }

    @Transactional(readOnly = true)
    public Page<AircraftResponse> list(Pageable pageable) {
        return repo.findAll(pageable).map(this::mapToResponse);
    }
}
