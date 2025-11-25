package flights_management.aeroops.dto.seat;

import flights_management.aeroops.enums.SeatClass;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record SeatRequestDTO(
        @NotNull(message = "Flight ID cannot be null")
        Long flightId,
        @NotNull(message = "Seat number cannot be null")
        @Pattern(
                //a seat must begin with a digit that is not 0  -> [1-9] e.g. 1A
                //a seat can have a second digit that can pe 0 -> [0-9] e.g. 20C
                // ? means that the second digit is optional -> e.g. 3F
                //a seat must end with a letter from A to G
                regexp = "^[1-9][0-9]?[A-G]$",
                message = "Seat number must be in format like '12A' (1-2 digits followed by a letter A–F)"
        )
        String seatNumber,
        @NotNull(message = "Seat class cannot be null")
        SeatClass seatClass
) { }
