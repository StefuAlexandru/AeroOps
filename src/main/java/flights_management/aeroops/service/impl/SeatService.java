package flights_management.aeroops.service.impl;

import flights_management.aeroops.dto.seat.SeatRequestDTO;
import flights_management.aeroops.dto.seat.SeatResponseDTO;
import flights_management.aeroops.entity.Flight;
import flights_management.aeroops.entity.Seat;
import flights_management.aeroops.enums.ErrorCode;
import flights_management.aeroops.enums.Status;
import flights_management.aeroops.error.BusinessException;
import flights_management.aeroops.error.ErrorModel;
import flights_management.aeroops.mapper.SeatMapper;
import flights_management.aeroops.repository.FlightRepository;
import flights_management.aeroops.repository.SeatRepository;
import flights_management.aeroops.repository.TicketRepository;
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
    private final TicketRepository ticketRepository;
    private final SeatMapper seatMapper;

    @Override
    public SeatResponseDTO createSeat(SeatRequestDTO seatRequestDTO) {
        Flight flight = validateForCreate(seatRequestDTO);
        Seat seat = seatMapper.toEntity(seatRequestDTO, flight);
        seatRepository.save(seat);
        return seatMapper.toResponse(seat);
    }

    @Override
    public List<SeatResponseDTO> getAllSeats() {
        return seatRepository.findAll()
                .stream()
                .map(seatMapper::toResponse)
                .toList();
    }

    @Override
    public SeatResponseDTO updateSeat(Long id, SeatRequestDTO seatRequestDTO) {
        Seat seatToUpdate = seatRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        List.of(new ErrorModel(ErrorCode.SEAT_NOT_FOUND))
                ));

        Flight newFlight = validateForUpdate(seatToUpdate, seatRequestDTO);

        seatToUpdate.setFlight(newFlight);
        seatToUpdate.setSeatNumber(seatRequestDTO.seatNumber());
        seatToUpdate.setSeatClass(seatRequestDTO.seatClass());
        seatToUpdate.setIsAvailable(seatRequestDTO.isAvailable());
        seatToUpdate.setUpdatedAt(Instant.now());

        seatRepository.save(seatToUpdate);

        return seatMapper.toResponse(seatToUpdate);
    }

    @Override
    public void deleteSeat(Long id) {
        List<ErrorModel> errors = new ArrayList<>();

        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        List.of(new ErrorModel(ErrorCode.SEAT_NOT_FOUND))
                ));

        validateSeatDeletable(seat, id, errors);

        if (!errors.isEmpty()) {
            throw new BusinessException(errors);
        }

        seatRepository.delete(seat);
    }

    private void validateSeatDeletable(Seat seat, Long seatId, List<ErrorModel> errors) {
        // You cannot delete a seat that already has tickets attached
        if (ticketRepository.existsBySeatId(seatId)) {
            errors.add(new ErrorModel(ErrorCode.SEAT_HAS_TICKETS_CANNOT_DELETE));
        }

        // You cannot delete a seat whose flight has already departed
        Flight flight = seat.getFlight();
        if (flight != null && flight.getStatus() == Status.DEPARTED) {
            errors.add(new ErrorModel(ErrorCode.FLIGHT_DEPARTED));
        }
    }

    private Flight validateForCreate(SeatRequestDTO dto) {
        List<ErrorModel> errors = new ArrayList<>();

        Flight flight = findFlightForCreate(dto.flightId(), errors);

        if (flight != null) {
            validateCreateConstraints(dto, flight, errors);
        }

        validateSeatUniqueness(dto, errors);

        if (!errors.isEmpty()) {
            throw new BusinessException(errors);
        }

        return flight;
    }

    private Flight findFlightForCreate(Long flightId, List<ErrorModel> errors) {
        Flight flight = flightRepository.findById(flightId).orElse(null);
        if (flight == null) {
            errors.add(new ErrorModel(ErrorCode.FLIGHT_NOT_FOUND));
        }
        return flight;
    }

    private void validateCreateConstraints(SeatRequestDTO dto, Flight flight, List<ErrorModel> errors) {
        long existingSeats = seatRepository.countByFlightId(dto.flightId());

        // You cannot create a seat if the aircraft capacity is exceeded
        if (existingSeats >= flight.getAircraft().getSeats()) {
            errors.add(new ErrorModel(ErrorCode.AIRCRAFT_CAPACITY_REACHED));
        }

        // You cannot create a seat on a cancelled flight
        if (flight.getStatus() == Status.CANCELLED) {
            errors.add(new ErrorModel(ErrorCode.FLIGHT_CANCELLED));
        }

        // You cannot create a seat on a departed flight
        if (flight.getStatus() == Status.DEPARTED) {
            errors.add(new ErrorModel(ErrorCode.FLIGHT_DEPARTED));
        }
    }

    private void validateSeatUniqueness(SeatRequestDTO dto, List<ErrorModel> errors) {
        if (seatRepository.existsByFlightIdAndSeatNumber(dto.flightId(), dto.seatNumber())) {
            errors.add(new ErrorModel(ErrorCode.SEAT_ALREADY_EXISTS_FOR_THIS_FLIGHT));
        }
    }

    private Flight validateForUpdate(Seat currentSeat, SeatRequestDTO dto) {
        List<ErrorModel> errors = new ArrayList<>();

        Flight currentFlight = currentSeat.getFlight();
        boolean hasTickets = ticketRepository.existsBySeatId(currentSeat.getId());

        validateCurrentFlightStatus(currentFlight, errors);

        Flight newFlight = findNewFlight(dto.flightId(), errors);

        if (newFlight != null) {
            validateNewFlightConstraints(currentSeat, dto, currentFlight, newFlight, errors);
        }

        validateTicketMoveRule(hasTickets, dto, currentFlight, errors);

        if (!errors.isEmpty()) {
            throw new BusinessException(errors);
        }

        return newFlight;
    }

    private void validateCurrentFlightStatus(Flight currentFlight, List<ErrorModel> errors) {
        if (currentFlight.getStatus() == Status.DEPARTED) {
            errors.add(new ErrorModel(ErrorCode.FLIGHT_DEPARTED));
        }
    }

    private Flight findNewFlight(Long flightId, List<ErrorModel> errors) {
        Flight newFlight = flightRepository.findById(flightId).orElse(null);
        if (newFlight == null) {
            errors.add(new ErrorModel(ErrorCode.FLIGHT_NOT_FOUND));
        }
        return newFlight;
    }

    private void validateNewFlightConstraints(Seat currentSeat, SeatRequestDTO dto, Flight currentFlight, Flight newFlight, List<ErrorModel> errors) {
        boolean changingFlight = !currentFlight.getId().equals(newFlight.getId());

        // You cannot update a seat if the target flight is cancelled
        if (newFlight.getStatus() == Status.CANCELLED) {
            errors.add(new ErrorModel(ErrorCode.FLIGHT_CANCELLED));
        }

        // You cannot switch to a flight that has already departed
        if (changingFlight && newFlight.getStatus() == Status.DEPARTED) {
            errors.add(new ErrorModel(ErrorCode.FLIGHT_DEPARTED));
        }

        // If switching flights, check the new aircraft capacity
        if (changingFlight) {
            long existingSeats = seatRepository.countByFlightId(newFlight.getId());
            if (existingSeats >= newFlight.getAircraft().getSeats()) {
                errors.add(new ErrorModel(ErrorCode.AIRCRAFT_CAPACITY_REACHED));
            }
        }

        // Ensure seat number is unique within the new flight (excluding the current seat)
        if (seatRepository.existsByFlightIdAndSeatNumberAndIdNot(newFlight.getId(), dto.seatNumber(), currentSeat.getId())) {
            errors.add(new ErrorModel(ErrorCode.SEAT_ALREADY_EXISTS_FOR_THIS_FLIGHT));
        }
    }

    private void validateTicketMoveRule(boolean hasTickets, SeatRequestDTO dto, Flight currentFlight, List<ErrorModel> errors) {
        // A seat with existing tickets cannot be moved to another flight
        if (hasTickets && isNotTheSameFlight(dto, currentFlight)) {
            errors.add(new ErrorModel(ErrorCode.SEAT_HAS_TICKETS_CANNOT_MOVE_TO_OTHER_FLIGHT));
        }
    }

    private static boolean isNotTheSameFlight(SeatRequestDTO dto, Flight currentFlight) {
        return !currentFlight.getId().equals(dto.flightId());
    }
}
