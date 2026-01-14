package flights_management.aeroops.mapper;

import flights_management.aeroops.dto.ticket.TicketDTO;
import flights_management.aeroops.dto.ticket.TicketRequestDTO;
import flights_management.aeroops.dto.ticket.TicketResponseDTO;
import flights_management.aeroops.entity.Booking;
import flights_management.aeroops.entity.Seat;
import flights_management.aeroops.entity.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TicketMapper {

    // RequestDTO -> Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "booking", source = "booking")
    @Mapping(target = "seat", source = "seat")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Ticket toEntity(TicketRequestDTO dto, Booking booking, Seat seat);

    // Entity -> ResponseDTO
    @Mapping(target = "code",          source = "booking.code")
    @Mapping(target = "bookingStatus", source = "booking.status")
    @Mapping(target = "seatNumber",    source = "seat.seatNumber")
    @Mapping(target = "seatClass",     source = "seat.seatClass")
    TicketResponseDTO toResponse(Ticket ticket);

    //Entity -> DTO
    @Mapping(target = "bookingId", source = "booking.id")
    @Mapping(target = "seat",      source = "seat")
    TicketDTO toDto(Ticket ticket);
}
