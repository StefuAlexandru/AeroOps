package flights_management.aeroops.dto.passenger;

import flights_management.aeroops.dto.flight.TicketDTO;
import java.util.List;

public record PassengerResponseDTO(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phone
) {
}
