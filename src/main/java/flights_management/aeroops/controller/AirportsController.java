package flights_management.aeroops.controller;

import flights_management.aeroops.dto.airport.AirportRequestDTO;
import flights_management.aeroops.dto.airport.AirportResponseDTO;
import flights_management.aeroops.service.IAirportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("api/airports")
@RequiredArgsConstructor
public class AirportsController {

    private final IAirportService service;


    @PostMapping("/create")
    public ResponseEntity<AirportResponseDTO> createAirport(@Valid @RequestBody AirportRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/get")
    public ResponseEntity<List<AirportResponseDTO>> getAllAirports() {
        return ResponseEntity.ok(service.getAll());
    }
}
