package flights_management.aeroops.mapper;

import flights_management.aeroops.dto.aircraft.AircraftDTO;
import flights_management.aeroops.dto.aircraft.AircraftRequestDTO;
import flights_management.aeroops.dto.aircraft.AircraftResponseDTO;
import flights_management.aeroops.entity.Aircraft;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AircraftMapper {

    // entity to dto
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "registration", expression = "java(requestDTO.registration().toUpperCase())")
    Aircraft toEntity(AircraftRequestDTO requestDTO);

    // entity to response DTO
    AircraftResponseDTO toResponseDTO(Aircraft entity);

    // entity to AircraftDTO
    AircraftDTO toAircraftDTO(Aircraft entity);
}