package flights_management.aeroops.dto.airport;

public record AircraftResponse(
        Long id, String registration, String type, String manufacturer, Integer seats
) {}
