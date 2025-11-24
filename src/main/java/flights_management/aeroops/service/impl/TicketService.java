package flights_management.aeroops.service.impl;

import flights_management.aeroops.dto.ticket.TicketRequestDTO;
import flights_management.aeroops.dto.ticket.TicketResponseDTO;
import flights_management.aeroops.entity.Booking;
import flights_management.aeroops.entity.Seat;
import flights_management.aeroops.entity.Ticket;
import flights_management.aeroops.error.BusinessException;
import flights_management.aeroops.error.ErrorModel;
import flights_management.aeroops.mapper.TicketMapper;
import flights_management.aeroops.repository.BookingRepository;
import flights_management.aeroops.repository.SeatsRepository;
import flights_management.aeroops.repository.TicketsRepository;
import flights_management.aeroops.service.ITicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketService implements ITicketService {
    private final TicketsRepository ticketsRepository;
    private final BookingRepository bookingRepository;
    private final SeatsRepository seatsRepository;
    private final TicketMapper ticketMapper;
    @Override
    public TicketResponseDTO createTicket(TicketRequestDTO ticketRequestDTO) {
        List<ErrorModel> errors = new ArrayList<>();
        Booking booking = bookingRepository.findById(ticketRequestDTO.bookingId()).orElse(null);
        if(booking == null){
            errors.add(new ErrorModel("BOOKING_NOT_FOUND", "Booking not found"));
        }
        Seat seat = seatsRepository.findById(ticketRequestDTO.seatId()).orElse(null);
        if(seat == null){
            errors.add(new ErrorModel("SEAT_NOT_FOUND", "Seat not found"));
        }

        if(!errors.isEmpty()) throw new BusinessException(errors);
        Ticket ticket = ticketMapper.toEntity(ticketRequestDTO,booking,seat);
        ticketsRepository.save(ticket);
        return ticketMapper.toResponse(ticket);
    }

    @Override
    public List<TicketResponseDTO> getAllTickets() {
        return ticketsRepository.findAll()
                .stream().map(ticketMapper::toResponse).toList();
    }
}
