package flights_management.aeroops.mapper;

import flights_management.aeroops.dto.seat.SeatDTO;
import flights_management.aeroops.dto.ticket.TicketDTO;
import flights_management.aeroops.dto.ticket.TicketRequestDTO;
import flights_management.aeroops.dto.ticket.TicketResponseDTO;
import flights_management.aeroops.entity.Booking;
import flights_management.aeroops.entity.Seat;
import flights_management.aeroops.entity.Ticket;
import flights_management.aeroops.enums.BookingStatus;
import flights_management.aeroops.enums.SeatClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;

import static flights_management.aeroops.util.TicketTestDataFactory.*;
import static org.junit.jupiter.api.Assertions.*;

class TicketMapperTest {

    private TicketMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new TicketMapperImpl();
    }

    @Test
    void toEntity_shouldMapFieldsCorrectly() {
        // Arrange
        TicketRequestDTO dto = aTicketRequestWithPrice(BigDecimal.valueOf(150));

        Booking booking = aBooking();

        Seat seat = new Seat();
        seat.setId(1L);
        seat.setSeatNumber("12A");
        seat.setSeatClass(SeatClass.BUSINESS);

        // Act
        Ticket ticket = mapper.toEntity(dto, booking, seat);

        // Assert
        assertAll("Ticket entity mapping from TicketRequestDTO + Booking + Seat",
                () -> assertNotNull(ticket, "Ticket should not be null"),
                () -> assertNull(ticket.getId(), "Id should be ignored when mapping"),
                () -> assertEquals(BigDecimal.valueOf(150), ticket.getPrice(), "Price should be copied from DTO"),
                () -> assertEquals(booking, ticket.getBooking(), "Booking should be set from argument"),
                () -> assertEquals(seat, ticket.getSeat(), "Seat should be set from argument"),
                () -> assertNull(ticket.getCreatedAt(), "createdAt should be null on mapping"),
                () -> assertNull(ticket.getUpdatedAt(), "updatedAt should be null on mapping")
        );
    }

    @Test
    void toResponse_shouldMapNestedFieldsCorrectly() {
        // Arrange
        Booking booking = aBooking();
        Seat seat = new Seat();
        seat.setId(2L);
        seat.setSeatNumber("14C");
        seat.setSeatClass(SeatClass.ECONOMY);
        seat.setIsAvailable(true);

        Ticket ticket = aTicket(booking, seat, BigDecimal.valueOf(200));
        Instant now = Instant.now();
        ticket.setCreatedAt(now);
        ticket.setUpdatedAt(now);

        // Act
        TicketResponseDTO response = mapper.toResponse(ticket);

        // Assert
        assertAll("TicketResponseDTO mapping from Ticket",
                () -> assertNotNull(response, "Response should not be null"),
                () -> assertEquals(1L, response.id(), "Id should be mapped from Ticket.id"),
                () -> assertEquals("123", response.code(), "Code should come from Booking.code"),
                () -> assertEquals(BookingStatus.HOLD, response.bookingStatus(), "Status should come from Booking.status"),
                () -> assertEquals(BigDecimal.valueOf(200), response.price(), "Price should match Ticket.price"),
                () -> assertEquals("14C", response.seatNumber(), "Seat number should come from Seat.seatNumber"),
                () -> assertEquals(SeatClass.ECONOMY, response.seatClass(), "Seat class should come from Seat.seatClass"),
                () -> assertEquals(ticket.getCreatedAt(), response.createdAt(), "createdAt should be mapped"),
                () -> assertEquals(ticket.getUpdatedAt(), response.updatedAt(), "updatedAt should be mapped")
        );
    }

    @Test
    void toDto_shouldMapFieldsCorrectly() {
        // Arrange
        Booking booking = aBooking();
        Seat seat = new Seat();
        seat.setId(2L);
        seat.setSeatNumber("14C");
        seat.setSeatClass(SeatClass.ECONOMY);
        seat.setIsAvailable(true);

        Ticket ticket = aTicket(booking, seat, BigDecimal.valueOf(200));

        // Act
        TicketDTO dto = mapper.toDto(ticket);

        // Assert
        assertAll("TicketDTO mapping from Ticket",
                () -> assertNotNull(dto, "TicketDTO should not be null"),
                () -> assertEquals(ticket.getId(), dto.id(), "Id should be mapped from Ticket.id"),
                () -> assertEquals(ticket.getBooking().getId(), dto.bookingId(), "bookingId in DTO should come from Booking.id"),
                () -> assertEquals(ticket.getPrice(), dto.price(), "Price should match Ticket.price"),
                () -> {
                    SeatDTO seatDTO = dto.seat();
                    assertAll("SeatDTO mapping inside TicketDTO",
                            () -> assertNotNull(seatDTO, "seatDTO should not be null"),
                            () -> assertEquals(ticket.getSeat().getId(), seatDTO.id(), "Seat id should match"),
                            () -> assertEquals(ticket.getSeat().getSeatNumber(), seatDTO.seatNumber(), "Seat number should match"),
                            () -> assertEquals(ticket.getSeat().getSeatClass(), seatDTO.seatClass(), "Seat class should match"),
                            () -> assertEquals(ticket.getSeat().getIsAvailable(), seatDTO.isAvailable(), "Seat availability should match")
                    );
                }
        );
    }
}
