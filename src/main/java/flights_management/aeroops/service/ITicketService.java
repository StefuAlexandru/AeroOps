package flights_management.aeroops.service;

import flights_management.aeroops.dto.flight.TicketRequestDTO;
import flights_management.aeroops.dto.flight.TicketResponseDTO;

import java.util.List;

public interface ITicketService {
    TicketResponseDTO createTicket(TicketRequestDTO ticketRequestDTO);
    List<TicketResponseDTO> getAllTickets();
}
