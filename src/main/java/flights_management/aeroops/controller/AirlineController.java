package flights_management.aeroops.controller;

import flights_management.aeroops.dto.airline.AirlineRequestDTO;
import flights_management.aeroops.dto.airline.AirlineResponseDTO;
import flights_management.aeroops.service.IAirlineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/airlines")
@RequiredArgsConstructor
public class AirlineController {

    private final IAirlineService airlineService;

    @GetMapping("/get")
    public ResponseEntity<List<AirlineResponseDTO>> getAllAirlines() {
        return ResponseEntity.ok(airlineService.getAllAirlines());
    }

    @PostMapping("/create")
    public ResponseEntity<AirlineResponseDTO> createAirline(
            @Valid @RequestBody AirlineRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(airlineService.createAirline(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AirlineResponseDTO> updateAirline(
            @PathVariable Long id,
            @Valid @RequestBody AirlineRequestDTO request) {

        AirlineResponseDTO response = airlineService.updateAirline(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAirline(@PathVariable Long id) {
        airlineService.deleteAirline(id);
        return ResponseEntity.noContent().build();
    }
}
