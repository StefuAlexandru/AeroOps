package flights_management.aeroops.service;

import flights_management.aeroops.dto.passenger.PassengerRequestDTO;
import flights_management.aeroops.dto.passenger.PassengerResponseDTO;

import java.util.List;

public interface IPassengerService {
    PassengerResponseDTO createPassenger(PassengerRequestDTO passengerRequestDTO);
    List<PassengerResponseDTO> getAllPassengers();
}