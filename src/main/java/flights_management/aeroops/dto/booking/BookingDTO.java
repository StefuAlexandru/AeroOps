package flights_management.aeroops.dto.booking;

public record BookingDTO (
    Long id,
    Long flightId,
    Long passagerId,
    Double price
) {}
