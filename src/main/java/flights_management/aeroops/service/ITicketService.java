package flights_management.aeroops.service;

import flights_management.aeroops.dto.ticket.TicketRequestDTO;
import flights_management.aeroops.dto.ticket.TicketResponseDTO;

import java.util.List;

public interface ITicketService {
    TicketResponseDTO createTicket(TicketRequestDTO ticketRequestDTO);
    List<TicketResponseDTO> getAllTickets();
    TicketResponseDTO updateTicket(Long id,TicketRequestDTO ticketRequestDTO);
    void deleteTicket(Long id);

}
