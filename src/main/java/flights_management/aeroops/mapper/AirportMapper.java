package flights_management.aeroops.mapper;

import flights_management.aeroops.dto.airport.AirportRequestDTO;
import flights_management.aeroops.dto.airport.AirportResponseDTO;
import flights_management.aeroops.dto.airport.AirportDTO;
import flights_management.aeroops.entity.Airport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AirportMapper {

    @Mapping(target = "id", ignore = true)
    Airport toEntity(AirportRequestDTO dto);

    AirportResponseDTO toResponse(Airport entity);

    // pentru compoziție internă
    AirportDTO toDto(Airport entity);
}