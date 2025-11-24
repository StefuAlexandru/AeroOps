package flights_management.aeroops.dto.ticket;

import flights_management.aeroops.dto.seat.SeatDTO;

import java.math.BigDecimal;

public record TicketDTO(Long id, Long bookingId, SeatDTO seat, BigDecimal price) { }