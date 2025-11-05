package flights_management.aeroops.service.impl;

import flights_management.aeroops.dto.flight.TicketRequestDTO;
import flights_management.aeroops.dto.flight.TicketResponseDTO;
import flights_management.aeroops.entity.Ticket;
import flights_management.aeroops.mapper.TicketMapper;
import flights_management.aeroops.repository.TicketsRepository;
import flights_management.aeroops.service.ITicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketService implements ITicketService {
    private final TicketsRepository ticketsRepository;
    private final TicketMapper ticketMapper;
    @Override
    public TicketResponseDTO createTicket(TicketRequestDTO ticketRequestDTO) {
        Ticket toSave = ticketMapper.toEntity(ticketRequestDTO);
        Ticket saved  = ticketsRepository.save(toSave);
        return ticketMapper.toResponse(saved);
    }

    @Override
    public List<TicketResponseDTO> getAllTickets() {
        return ticketsRepository.findAll()
                .stream().map(ticketMapper::toResponse).toList();
    }
}
