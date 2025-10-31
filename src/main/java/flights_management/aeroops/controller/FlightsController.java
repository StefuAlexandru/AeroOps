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
@RequestMapping("api/flights")
@RequiredArgsConstructor
public class FlightsController {

    public final IFlightService flightService;

    @PostMapping("/create")
    public ResponseEntity<FlightResponseDTO> createFlight(
            @Valid @RequestBody FlightRequestDTO flightRequestDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(flightService.createFlight(flightRequestDTO));
    }

    @GetMapping("/get")
    public ResponseEntity<List<FlightResponseDTO>> getAllFlights(){
        List<FlightResponseDTO> flights = flightService.getAllFlights();
        return ResponseEntity.ok(flights);
    }

}
