package flights_management.aeroops.dto.airline;

public record AirlineResponseDTO(
        Long id,
        String name,
        String iataCode,
        String country
) { }
