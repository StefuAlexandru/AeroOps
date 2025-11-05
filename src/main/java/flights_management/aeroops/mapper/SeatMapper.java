package flights_management.aeroops.mapper;

import flights_management.aeroops.dto.flight.SeatDTO;
import flights_management.aeroops.dto.flight.SeatRequestDTO;
import flights_management.aeroops.dto.flight.SeatResponseDTO;
import flights_management.aeroops.entity.Seat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SeatMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isAvailable", constant = "true")
    Seat toEntity(SeatRequestDTO dto);
    SeatResponseDTO toResponse(Seat seat);
    SeatDTO toDto(Seat seat);
}
