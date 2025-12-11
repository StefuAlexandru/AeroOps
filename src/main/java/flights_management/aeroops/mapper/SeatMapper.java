package flights_management.aeroops.mapper;

import flights_management.aeroops.dto.seat.SeatDTO;
import flights_management.aeroops.dto.seat.SeatRequestDTO;
import flights_management.aeroops.dto.seat.SeatResponseDTO;
import flights_management.aeroops.entity.Flight;
import flights_management.aeroops.entity.Seat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SeatMapper {

    // RequestDTO -> Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "flight", source = "flight")
    @Mapping(target = "isAvailable", constant = "true")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Seat toEntity(SeatRequestDTO dto, Flight flight);

    // Entity -> ResponseDTO
    @Mapping(target = "flightNumber", source = "flight.flightNumber")
    SeatResponseDTO toResponse(Seat seat);

    //Entity -> DTO
    @Mapping(target="flightId", source = "flight.id" )
    SeatDTO toDto(Seat seat);
}
