package flights_management.aeroops.mapper;

import flights_management.aeroops.dto.airline.AirlineRequestDTO;
import flights_management.aeroops.dto.airline.AirlineResponseDTO;
import flights_management.aeroops.entity.Airline;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AirlineMapper {

    @Mapping(target = "id", ignore = true)
    Airline toEntity(AirlineRequestDTO dto);

    AirlineResponseDTO toResponse(Airline airline);
}
