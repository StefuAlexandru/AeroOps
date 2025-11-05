package flights_management.aeroops.service;

import flights_management.aeroops.dto.flight.SeatRequestDTO;
import flights_management.aeroops.dto.flight.SeatResponseDTO;

import java.util.List;

public interface ISeatService {
    SeatResponseDTO createSeat(SeatRequestDTO seatRequestDTO);
    List<SeatResponseDTO> getAllSeats();
}
