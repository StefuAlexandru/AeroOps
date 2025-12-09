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
@RequestMapping("api/airport")
@RequiredArgsConstructor
public class AirportController {

    private final IAirportService airportService;

    @PostMapping
    public ResponseEntity<AirportResponseDTO> createAirport(
            @Valid @RequestBody AirportRequestDTO airportRequestDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(airportService.createAirport(airportRequestDTO));
    }

    @GetMapping
    public ResponseEntity<List<AirportResponseDTO>> getAllAirports(){
        return ResponseEntity.ok(airportService.getAllAirports());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AirportResponseDTO> updateAirport(
            @PathVariable Long id,
            @Valid @RequestBody AirportRequestDTO request) {

        AirportResponseDTO response = airportService.updateAirport(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAirport(@PathVariable Long id) {
        airportService.deleteAirport(id);
        return ResponseEntity.noContent().build();
    }
}
