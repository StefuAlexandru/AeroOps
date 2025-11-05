package flights_management.aeroops.controller;

import flights_management.aeroops.dto.flight.TicketRequestDTO;
import flights_management.aeroops.dto.flight.TicketResponseDTO;
import flights_management.aeroops.service.ITicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/tickets")
@RequiredArgsConstructor
public class TicketsController {
    private final ITicketService ticketService;

    @PostMapping("/create")
    public ResponseEntity<TicketResponseDTO> createTicket(
            @Valid @RequestBody TicketRequestDTO ticketRequestDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketService.createTicket(ticketRequestDTO));
    }

    @GetMapping("/get")
    public ResponseEntity<List<TicketResponseDTO>> getAllTickets(){
        List<TicketResponseDTO> tickets = ticketService.getAllTickets();
        return ResponseEntity.ok(tickets);
    }
}
