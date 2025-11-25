package flights_management.aeroops.dto.seat;

import flights_management.aeroops.enums.SeatClass;

//de la Aircraft : model, seatCapacity
public record SeatResponseDTO(
        Long id,
        String model,
        Integer seatCapacity,
        String seatNumber,
        SeatClass seatClass,
        Boolean isAvailable
) { }
