package flights_management.aeroops.dto.ticket;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

import static flights_management.aeroops.dto.ticket.TicketRequestMessages.*;

public record TicketRequestDTO(
        @NotNull(message = BOOKING_ID_NOT_NULL)
        Long bookingId,
        @NotNull(message = SEAT_ID_NOT_NULL)
        Long seatId,
        @NotNull(message = PRICE_NOT_NULL)
        @DecimalMin(value = "0.00", inclusive = false, message = PRICE_MIN_MESSAGE)
        @Digits(integer = 10, fraction = 2, message = PRICE_DIGITS_MESSAGE)
        BigDecimal price
) { }
