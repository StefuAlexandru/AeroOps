package flights_management.aeroops.dto.booking;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record BookingRequestDTO(

        @NotNull(message = "flightId is required")
        @Positive(message = "flightId must be a positive number")
        Long flightId,

        @NotNull(message = "passengerId is required")
        @Positive(message = "passengerId must be a positive number")
        Long passengerId,

        @NotNull(message = "priceTotal is required")
        @DecimalMin(value = "0.01", inclusive = true, message = "priceTotal must be greater than zero")
        BigDecimal priceTotal

) {
}