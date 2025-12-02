package flights_management.aeroops.dto.aircraft;


public record AircraftDTO(
        Long id,
        String registration,
        String type,
        String manufacturer,
        Integer seats
) {
}
