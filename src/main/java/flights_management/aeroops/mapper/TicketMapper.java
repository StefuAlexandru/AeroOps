package flights_management.aeroops.mapper;

import flights_management.aeroops.dto.flight.TicketDTO;
import flights_management.aeroops.dto.flight.TicketRequestDTO;
import flights_management.aeroops.dto.flight.TicketResponseDTO;
import flights_management.aeroops.entity.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { SeatResolver.class })
public interface TicketMapper {
    // din request: setăm Seat pe baza seatId; id-ul entității se ignoră la creare
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "seat", source = "seatId")  // SeatResolver.fromId(Long) este folosit automat
    Ticket toEntity(TicketRequestDTO dto);

    // în response: expunem doar seatNumber și seatClass din Seat
    @Mapping(target = "seatNumber", source = "seat.seatNumber")
    @Mapping(target = "seatClass",  source = "seat.seatClass")
    TicketResponseDTO toResponse(Ticket ticket);

    TicketDTO toDto(Ticket ticket);
}
