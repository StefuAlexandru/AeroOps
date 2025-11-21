package flights_management.aeroops.dto.airport;

public record AirportResponseDTO(
        Long id, String iataCode, String name, String city, String country, String timeZoneId
) {}