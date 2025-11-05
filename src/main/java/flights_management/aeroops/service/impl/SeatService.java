package flights_management.aeroops.service.impl;

import flights_management.aeroops.dto.flight.SeatRequestDTO;
import flights_management.aeroops.dto.flight.SeatResponseDTO;
import flights_management.aeroops.entity.Seat;
import flights_management.aeroops.mapper.SeatMapper;
import flights_management.aeroops.repository.SeatsRepository;
import flights_management.aeroops.service.ISeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SeatService implements ISeatService {
    private final SeatsRepository seatsRepository;
    private final SeatMapper seatMapper;

    @Override
    public SeatResponseDTO createSeat(SeatRequestDTO seatRequestDTO) {
        Seat toSave = seatMapper.toEntity(seatRequestDTO);
        Seat saved = seatsRepository.save(toSave);
        return seatMapper.toResponse(saved);
    }

    @Override
    public List<SeatResponseDTO> getAllSeats() {
        return seatsRepository.findAll()
                .stream().map(seatMapper::toResponse).toList();
    }
}
