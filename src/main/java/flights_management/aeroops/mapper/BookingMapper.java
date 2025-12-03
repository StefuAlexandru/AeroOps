package flights_management.aeroops.mapper;

import flights_management.aeroops.dto.booking.BookingRequestDTO;
import flights_management.aeroops.dto.booking.BookingResponseDTO;
import flights_management.aeroops.entity.Booking;
import flights_management.aeroops.entity.Flight;
import flights_management.aeroops.entity.Passenger;
import flights_management.aeroops.util.PnrGenerator;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    // entity to dto
    @Mapping(target = "flightId",source = "flight.id")
    @Mapping(target = "passengerId",source = "passenger.id")
    BookingResponseDTO toResponse(Booking entity);

    // dto to entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", expression = "java(pnrGenerator.generate())")
    @Mapping(target = "flight", expression = "java(flight)")
    @Mapping(target = "passenger", expression = "java(passenger)")
    @Mapping(target = "status", constant = "HOLD")
    @Mapping(target = "priceTotal", source = "req.priceTotal")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Booking toEntity(BookingRequestDTO req, Flight flight, Passenger passenger, @Context PnrGenerator pnrGenerator);
}
