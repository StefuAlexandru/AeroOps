package flights_management.aeroops.controller;

import flights_management.aeroops.dto.airport.AircraftRequest;
import flights_management.aeroops.dto.airport.AircraftResponse;
import flights_management.aeroops.service.impl.AircraftService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aircraft")
public class AircraftController {
    private final AircraftService service;

    public AircraftController(AircraftService service) { this.service = service; }

    @GetMapping
    public List<AircraftResponse> list() {
        return service.listAircraft();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AircraftResponse create(@RequestBody @Valid AircraftRequest req) {
        return service.createAircraft(req);
    }

    @GetMapping
    public Page<AircraftResponse> list(Pageable pageable) {
        return service.list(pageable);
    }
}
