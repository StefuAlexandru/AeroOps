package flights_management.aeroops.dto.passenger;


import java.util.List;

public record PassengerDTO(
        Long id,
        String firstName,
        String lastName
) {
}
