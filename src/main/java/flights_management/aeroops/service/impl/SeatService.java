package flights_management.aeroops.service.impl;

import flights_management.aeroops.dto.seat.SeatRequestDTO;
import flights_management.aeroops.dto.seat.SeatResponseDTO;
import flights_management.aeroops.entity.Aircraft;
import flights_management.aeroops.entity.Seat;
import flights_management.aeroops.error.BusinessException;
import flights_management.aeroops.error.ErrorModel;
import flights_management.aeroops.mapper.SeatMapper;
import flights_management.aeroops.repository.AircraftRepository;
import flights_management.aeroops.repository.SeatsRepository;
import flights_management.aeroops.service.ISeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SeatService implements ISeatService {
    private final SeatsRepository seatsRepository;
    private final AircraftRepository aircraftRepository;
    private final SeatMapper seatMapper;

    @Override
    public SeatResponseDTO createSeat(SeatRequestDTO seatRequestDTO) {
        List<ErrorModel> errors = new ArrayList<>();
        Aircraft aircraft = aircraftRepository.findById(seatRequestDTO.aircraftId()).orElse(null);
        if(aircraft == null){
            errors.add(new ErrorModel("AIRCRAFT_NOT_FOUND", "Aircraft not found"));
        }

        if(!errors.isEmpty()) throw new BusinessException(errors);
        Seat seat = seatMapper.toEntity(seatRequestDTO,aircraft);
        seatsRepository.save(seat);
        return seatMapper.toResponse(seat);
    }

    @Override
    public List<SeatResponseDTO> getAllSeats() {
        return seatsRepository.findAll()
                .stream().map(seatMapper::toResponse).toList();
    }
}
