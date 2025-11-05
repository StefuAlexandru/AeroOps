package flights_management.aeroops.dto.flight;

import flights_management.aeroops.entity.Seat;

public record TicketDTO(Long id, Long bookingId, Seat seat, Double price) { }
