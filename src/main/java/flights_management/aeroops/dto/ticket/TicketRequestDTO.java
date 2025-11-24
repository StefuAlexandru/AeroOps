package flights_management.aeroops.dto.ticket;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TicketRequestDTO(
        @NotNull(message = "Booking ID cannot be null")
        Long bookingId,
        @NotNull(message = "Seat ID cannot be null")
        Long seatId,
        @NotNull(message = "Price cannot be null")
        @DecimalMin(value = "0.00", inclusive = false, message = "Price must be > 0")
        @Digits(integer = 10, fraction = 2, message = "Price can have at most 10 digits before the comma, and 2 after it")
        BigDecimal price
) { }
