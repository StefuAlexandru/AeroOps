package flights_management.aeroops.mapper;

import flights_management.aeroops.dto.seat.SeatDTO;
import flights_management.aeroops.dto.seat.SeatRequestDTO;
import flights_management.aeroops.dto.seat.SeatResponseDTO;
import flights_management.aeroops.entity.Aircraft;
import flights_management.aeroops.entity.Seat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SeatMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "aircraft", expression = "java(aircraft)")
    @Mapping(target = "isAvailable", constant = "true")
    Seat toEntity(SeatRequestDTO dto, Aircraft aircraft);
    SeatResponseDTO toResponse(Seat seat);
    SeatDTO toDto(Seat seat);
}
