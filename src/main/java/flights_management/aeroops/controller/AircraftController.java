package flights_management.aeroops.controller;

import flights_management.aeroops.dto.aircraft.AircraftRequestDTO;
import flights_management.aeroops.dto.aircraft.AircraftResponseDTO;
import flights_management.aeroops.service.IAircraftService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/aircraft")
@RequiredArgsConstructor
public class AircraftController {

    private final IAircraftService aircraftService;

    @GetMapping
    public ResponseEntity<List<AircraftResponseDTO>> getAllAircrafts() {
        List<AircraftResponseDTO> aircrafts = aircraftService.getAllAircrafts();
        return ResponseEntity.ok(aircrafts);
    }

    @PostMapping
    public ResponseEntity<AircraftResponseDTO> createAircraft(@RequestBody @Valid AircraftRequestDTO requestDTO) {
        AircraftResponseDTO responseDTO = aircraftService.createAircraft(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AircraftResponseDTO> updateAircraft(@PathVariable Long id, @Valid @RequestBody AircraftRequestDTO requestDTO) {
        AircraftResponseDTO updated = aircraftService.updateAircraft(id, requestDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<AircraftResponseDTO> deleteAircraft(@PathVariable Long id) {
        aircraftService.deleteAircraft(id);
        return ResponseEntity.noContent().build();
    }
}
