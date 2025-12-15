package flights_management.aeroops.controller;

import flights_management.aeroops.dto.flight.FlightRequestDTO;
import flights_management.aeroops.dto.flight.FlightResponseDTO;
import flights_management.aeroops.service.IFlightService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/flight")
@RequiredArgsConstructor
public class FlightController {

    public final IFlightService flightService;

    @PostMapping
    public ResponseEntity<FlightResponseDTO> createFlight(
            @Valid @RequestBody FlightRequestDTO flightRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(flightService.createFlight(flightRequestDTO));
    }

    @GetMapping
    public ResponseEntity<List<FlightResponseDTO>> getAllFlights() {
        List<FlightResponseDTO> flights = flightService.getAllFlights();
        return ResponseEntity.ok(flights);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlightResponseDTO> updateFlight(
            @PathVariable Long id,
            @Valid @RequestBody FlightRequestDTO flightRequestDTO
    ) {
        FlightResponseDTO updated = flightService.updateFlight(id, flightRequestDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        flightService.deleteFlight(id);
        return ResponseEntity.noContent().build();
    }

}
