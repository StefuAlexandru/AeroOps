package flights_management.aeroops.service;

import flights_management.aeroops.dto.seat.SeatRequestDTO;
import flights_management.aeroops.dto.seat.SeatResponseDTO;

import java.util.List;

public interface ISeatService {
    SeatResponseDTO createSeat(SeatRequestDTO seatRequestDTO);
    List<SeatResponseDTO> getAllSeats();
}
