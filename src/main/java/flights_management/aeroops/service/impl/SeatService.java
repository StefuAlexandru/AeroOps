package flights_management.aeroops.service.impl;

import flights_management.aeroops.dto.seat.SeatRequestDTO;
import flights_management.aeroops.dto.seat.SeatResponseDTO;
import flights_management.aeroops.entity.Flight;
import flights_management.aeroops.entity.Seat;
import flights_management.aeroops.error.BusinessException;
import flights_management.aeroops.error.ErrorModel;
import flights_management.aeroops.mapper.SeatMapper;
import flights_management.aeroops.repository.FlightsRepository;
import flights_management.aeroops.repository.SeatRepository;
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
    private final SeatRepository seatRepository;
    private final FlightsRepository flightRepository;
    private final SeatMapper seatMapper;

    @Override
    public SeatResponseDTO createSeat(SeatRequestDTO seatRequestDTO) {
        List<ErrorModel> errors = new ArrayList<>();
        Flight flight = flightRepository.findById(seatRequestDTO.flightId()).orElse(null);
        if(flight == null){
            errors.add(new ErrorModel("FLIGHT_NOT_FOUND", "Flight not found"));
        }

        if(!errors.isEmpty()) throw new BusinessException(errors);
        Seat seat = seatMapper.toEntity(seatRequestDTO,flight);
        seatRepository.save(seat);
        return seatMapper.toResponse(seat);
    }

    @Override
    public List<SeatResponseDTO> getAllSeats() {
        return seatRepository.findAll()
                .stream().map(seatMapper::toResponse).toList();
    }
}
