package flights_management.aeroops.service.impl;

import flights_management.aeroops.dto.seat.SeatRequestDTO;
import flights_management.aeroops.dto.seat.SeatResponseDTO;
import flights_management.aeroops.entity.Flight;
import flights_management.aeroops.entity.Seat;
import flights_management.aeroops.enums.ErrorCode;
import flights_management.aeroops.error.BusinessException;
import flights_management.aeroops.error.ErrorModel;
import flights_management.aeroops.mapper.SeatMapper;
import flights_management.aeroops.repository.FlightRepository;
import flights_management.aeroops.repository.SeatRepository;
import flights_management.aeroops.service.ISeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SeatService implements ISeatService {
    private final SeatRepository seatRepository;
    private final FlightRepository flightRepository;
    private final SeatMapper seatMapper;

    @Override
    public SeatResponseDTO createSeat(SeatRequestDTO seatRequestDTO) {
        ValidatedSeatData validatedSeatData = validateAndFetch(seatRequestDTO);
        Seat seat = seatMapper.toEntity(seatRequestDTO,validatedSeatData.flight());
        seatRepository.save(seat);
        return seatMapper.toResponse(seat);
    }

    @Override
    public List<SeatResponseDTO> getAllSeats() {
        return seatRepository.findAll()
                .stream().map(seatMapper::toResponse).toList();
    }

    @Override
    public SeatResponseDTO updateSeat(Long id, SeatRequestDTO seatRequestDTO) {
        Seat seatToUpdate =  seatRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        List.of(new ErrorModel(ErrorCode.SEAT_NOT_FOUND))
                ));

        ValidatedSeatData validatedSeatData = validateAndFetch(seatRequestDTO);

        seatToUpdate.setFlight(validatedSeatData.flight());
        seatToUpdate.setSeatNumber(seatRequestDTO.seatNumber());
        seatToUpdate.setSeatClass(seatRequestDTO.seatClass());
        seatToUpdate.setUpdatedAt(Instant.now());

        seatRepository.save(seatToUpdate);

        return seatMapper.toResponse(seatToUpdate);
    }

    @Override
    public void deleteSeat(Long id) {
        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        List.of(new ErrorModel(ErrorCode.SEAT_NOT_FOUND))
                ));
        seatRepository.delete(seat);
    }




    private record ValidatedSeatData(Flight flight){ }

    private ValidatedSeatData validateAndFetch(SeatRequestDTO seatRequestDTO){
        List<ErrorModel> errors = new ArrayList<>();
        Flight flight = flightRepository.findById(seatRequestDTO.flightId())
                .orElse(null);
        if(flight == null){
            errors.add(new ErrorModel(ErrorCode.FLIGHT_NOT_FOUND));
        }
        if(!errors.isEmpty()){
            throw new BusinessException(errors);
        }

        return new ValidatedSeatData(flight);
    }
}
