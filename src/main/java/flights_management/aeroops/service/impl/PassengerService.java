package flights_management.aeroops.service.impl;

import flights_management.aeroops.dto.passenger.PassengerRequestDTO;
import flights_management.aeroops.dto.passenger.PassengerResponseDTO;
import flights_management.aeroops.entity.Passenger;
import flights_management.aeroops.mapper.PassengerMapper;
import flights_management.aeroops.repository.PassengerRepository;
import flights_management.aeroops.service.IPassengerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PassengerService implements IPassengerService {

    private final PassengerRepository passengerRepository;
    private final PassengerMapper passengerMapper;

    @Override
    public PassengerResponseDTO createPassenger(PassengerRequestDTO passengerRequestDTO) {
        Passenger passenger = passengerMapper.toEntity(passengerRequestDTO);
        passengerRepository.save(passenger);
        return passengerMapper.toResponseDTO(passenger);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PassengerResponseDTO> getAllPassengers() {
        return passengerRepository.findAll()
                .stream().map(passengerMapper::toResponseDTO).toList();
    }
}
