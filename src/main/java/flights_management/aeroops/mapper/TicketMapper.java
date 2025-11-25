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
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "booking", expression = "java(booking)")
    @Mapping(target = "seat", expression = "java(seat)")
    Ticket toEntity(TicketRequestDTO dto, Booking booking, Seat seat);

    @Mapping(target = "id",            source = "id")
    @Mapping(target = "code",          source = "booking.code")
    @Mapping(target = "status",        source = "booking.status")
    @Mapping(target = "price",         source = "price")
    @Mapping(target = "seatNumber",    source = "seat.seatNumber")
    @Mapping(target = "seatClass",     source = "seat.seatClass")
    TicketResponseDTO toResponse(Ticket ticket);

    TicketDTO toDto(Ticket ticket);
}
