package flights_management.aeroops.dto.flight;

import flights_management.aeroops.enums.SeatClass;

public record TicketResponseDTO(Long id, Long bookingId, Double price, String seatNumber, SeatClass seatClass) { }
