package flights_management.aeroops.controller;

import flights_management.aeroops.dto.seat.SeatRequestDTO;
import flights_management.aeroops.dto.seat.SeatResponseDTO;
import flights_management.aeroops.dto.ticket.TicketRequestDTO;
import flights_management.aeroops.dto.ticket.TicketResponseDTO;
import flights_management.aeroops.service.ITicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/ticket")
@RequiredArgsConstructor
public class TicketController {
    private final ITicketService ticketService;

    @PostMapping
    public ResponseEntity<TicketResponseDTO> createTicket(
            @Valid @RequestBody TicketRequestDTO ticketRequestDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketService.createTicket(ticketRequestDTO));
    }

    @GetMapping
    public ResponseEntity<List<TicketResponseDTO>> getAllTickets(){
        List<TicketResponseDTO> tickets = ticketService.getAllTickets();
        return ResponseEntity.ok(tickets);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> updateTicket(
            @PathVariable Long id,
            @Valid @RequestBody TicketRequestDTO ticketRequestDTO
    ){
        TicketResponseDTO ticketResponseDTO = ticketService.updateTicket(id,ticketRequestDTO);
        return ResponseEntity.ok(ticketResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TicketResponseDTO> deleteTicket(@PathVariable Long id){
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }
}

