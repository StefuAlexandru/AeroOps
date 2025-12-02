package flights_management.aeroops.dto.aircraft;

public record AircraftResponseDTO(
        Long id,
        String registration,
        String type,
        String manufacturer,
        Integer seats
) {
}
