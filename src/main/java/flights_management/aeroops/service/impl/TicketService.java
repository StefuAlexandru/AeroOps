package flights_management.aeroops.service.impl;

import flights_management.aeroops.dto.ticket.TicketRequestDTO;
import flights_management.aeroops.dto.ticket.TicketResponseDTO;
import flights_management.aeroops.entity.Booking;
import flights_management.aeroops.entity.Seat;
import flights_management.aeroops.entity.Ticket;
import flights_management.aeroops.enums.ErrorCode;
import flights_management.aeroops.error.BusinessException;
import flights_management.aeroops.error.ErrorModel;
import flights_management.aeroops.mapper.TicketMapper;
import flights_management.aeroops.repository.BookingRepository;
import flights_management.aeroops.repository.SeatRepository;
import flights_management.aeroops.repository.TicketRepository;
import flights_management.aeroops.service.ITicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketService implements ITicketService {
    private final TicketRepository ticketRepository;
    private final BookingRepository bookingRepository;
    private final SeatRepository seatsRepository;
    private final TicketMapper ticketMapper;

    @Override
    public TicketResponseDTO createTicket(TicketRequestDTO ticketRequestDTO) {
        ValidatedTicketData validatedTicketData = validateAndFetch(ticketRequestDTO);

        Ticket ticket = ticketMapper.toEntity(
                ticketRequestDTO,
                validatedTicketData.booking(),
                validatedTicketData.seat());

        ticketRepository.save(ticket);
        return ticketMapper.toResponse(ticket);
    }

    @Override
    public List<TicketResponseDTO> getAllTickets() {
        return ticketRepository.findAll()
                .stream().map(ticketMapper::toResponse).toList();
    }

    @Override
    public TicketResponseDTO updateTicket(Long id, TicketRequestDTO ticketRequestDTO) {
        Ticket ticketToUpdate =  ticketRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        List.of(new ErrorModel(ErrorCode.TICKET_NOT_FOUND))
                ));

        ValidatedTicketData validatedTicketData = validateAndFetch(ticketRequestDTO);

        ticketToUpdate.setSeat(validatedTicketData.seat());
        ticketToUpdate.setBooking(validatedTicketData.booking());
        ticketToUpdate.setPrice(ticketRequestDTO.price());
        ticketToUpdate.setUpdatedAt(Instant.now());

        ticketRepository.save(ticketToUpdate);
        return ticketMapper.toResponse(ticketToUpdate);

    }

    @Override
    public void deleteTicket(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        List.of(new ErrorModel(ErrorCode.TICKET_NOT_FOUND))
                ));
        ticketRepository.delete(ticket);
    }



    private record ValidatedTicketData(Booking booking,Seat seat){ }

    private ValidatedTicketData validateAndFetch(TicketRequestDTO ticketRequestDTO){
        List<ErrorModel> errors = new ArrayList<>();
        Booking booking = bookingRepository.findById(ticketRequestDTO.bookingId()).orElse(null);
        if(booking == null){
            errors.add(new ErrorModel(ErrorCode.BOOKING_NOT_FOUND));
        }
        Seat seat =  seatsRepository.findById(ticketRequestDTO.seatId()).orElse(null);
        if(seat == null){
            errors.add(new ErrorModel(ErrorCode.SEAT_NOT_FOUND));
        }
        if (!errors.isEmpty()) {
            throw new BusinessException(errors);
        }

        return new ValidatedTicketData(booking, seat);
    }
}

