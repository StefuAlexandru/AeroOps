package flights_management.aeroops.mapper;

import flights_management.aeroops.dto.booking.BookingRequestDTO;
import flights_management.aeroops.dto.booking.BookingResponseDTO;
import flights_management.aeroops.entity.Booking;
import flights_management.aeroops.entity.Flight;
import flights_management.aeroops.entity.Passenger;
import flights_management.aeroops.util.PnrGenerator;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PnrGenerator.class})
public interface BookingMapper {

    // entity to dto
    @Mapping(source = "flight.id", target = "flightId")
    @Mapping(source = "passenger.id", target = "passengerId")
    BookingResponseDTO toResponse(Booking entity);

    // dto to entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "code", expression = "java(pnrGenerator.generate())")
    @Mapping(target = "flight", expression = "java(flight)")
    @Mapping(target = "passenger", expression = "java(passenger)")
    @Mapping(target = "status", constant = "HOLD")
    @Mapping(target = "priceTotal", source = "req.priceTotal")
    @Mapping(target = "createdAt", expression = "java(java.time.Instant.now())")
    Booking toEntity(BookingRequestDTO req, Flight flight, Passenger passenger);
}
