package flights_management.aeroops.dto.seat;

import flights_management.aeroops.enums.SeatClass;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import static flights_management.aeroops.dto.seat.SeatRequestMessages.*;

public record SeatRequestDTO(
        @NotNull(message = FLIGHT_ID_NOT_NULL)
        Long flightId,
        @Pattern(
                //a seat must begin with a digit that is not 0  -> [1-9] e.g. 1A
                //a seat can have a second digit that can pe 0 -> [0-9] e.g. 20C
                // ? means that the second digit is optional -> e.g. 3F
                //a seat must end with a letter from A to G
                regexp = "^[1-9][0-9]?[A-G]$",
                message = SEAT_NUMBER_INVALID
        )
        @NotNull(message = SEAT_NUMBER_NOT_NULL)
        String seatNumber,
        @NotNull(message = SEAT_CLASS_NOT_NULL)
        SeatClass seatClass,
        @NotNull(message = SEAT_AVAILABILITY_NOT_NULL)
        Boolean isAvailable
) { }
