package flights_management.aeroops.dto.ticket;

import flights_management.aeroops.enums.BookingStatus;
import flights_management.aeroops.enums.SeatClass;

import java.math.BigDecimal;
import java.time.Instant;

//de la Booking : code, bookingStatus
//de la Seat : seatNumber, seatClass
public record TicketResponseDTO(
        Long id,
        String code,
        BookingStatus bookingStatus,
        BigDecimal price,
        String seatNumber,
        SeatClass seatClass,
        Instant createdAt,
        Instant updatedAt
) { }
