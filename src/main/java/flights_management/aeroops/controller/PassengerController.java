package flights_management.aeroops.controller;

import flights_management.aeroops.dto.passenger.PassengerRequestDTO;
import flights_management.aeroops.dto.passenger.PassengerResponseDTO;
import flights_management.aeroops.service.IPassengerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/passenger")
@RequiredArgsConstructor
public class PassengerController {
    private final IPassengerService passengerService;

    @PostMapping
    public ResponseEntity<PassengerResponseDTO> createPassenger(
            @Valid @RequestBody PassengerRequestDTO passengerRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(passengerService.createPassenger(passengerRequestDTO));
    }

    @GetMapping
    public ResponseEntity<List<PassengerResponseDTO>> getPassengers() {
        return ResponseEntity.ok(passengerService.getAllPassengers());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PassengerResponseDTO> updatePassenger(
            @PathVariable Long id,
            @Valid @RequestBody PassengerRequestDTO request
    ) {
        PassengerResponseDTO updated = passengerService.updatePassenger(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePassenger(@PathVariable Long id) {
        passengerService.deletePassenger(id);
        return ResponseEntity.noContent().build();
    }

}
