package flights_management.aeroops.controller;

import flights_management.aeroops.dto.seat.SeatRequestDTO;
import flights_management.aeroops.dto.seat.SeatResponseDTO;
import flights_management.aeroops.service.ISeatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/seats")
@RequiredArgsConstructor
public class SeatsController {
    private final ISeatService seatService;

    @PostMapping("/create")
    public ResponseEntity<SeatResponseDTO> createSeat(
            @Valid @RequestBody SeatRequestDTO seatRequestDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(seatService.createSeat(seatRequestDTO));
    }

    @GetMapping("/get")
    public ResponseEntity<List<SeatResponseDTO>> getAllSeats(){
        List<SeatResponseDTO> seats = seatService.getAllSeats();
        return ResponseEntity.ok(seats);
    }
}
