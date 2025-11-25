package flights_management.aeroops.mapper;


import flights_management.aeroops.dto.flight.TicketDTO;
import flights_management.aeroops.dto.passenger.PassengerDTO;
import flights_management.aeroops.dto.passenger.PassengerRequestDTO;
import flights_management.aeroops.dto.passenger.PassengerResponseDTO;
import flights_management.aeroops.entity.Passenger;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring" )
public interface PassengerMapper {

    // RequestDTO -> Entity

    @Mapping(target = "id", ignore = true)
    Passenger toEntity(PassengerRequestDTO passengerRequestDTO);

    // Entity -> ResponseDTO

    PassengerResponseDTO toResponseDTO(Passenger passenger);

    // Entity -> DTO

    PassengerDTO toDTO(Passenger passenger);

}
