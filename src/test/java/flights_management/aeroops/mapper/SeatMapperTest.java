package flights_management.aeroops.mapper;

import flights_management.aeroops.dto.seat.SeatDTO;
import flights_management.aeroops.dto.seat.SeatRequestDTO;
import flights_management.aeroops.dto.seat.SeatResponseDTO;
import flights_management.aeroops.entity.Flight;
import flights_management.aeroops.entity.Seat;
import flights_management.aeroops.enums.SeatClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static flights_management.aeroops.util.SeatTestDataFactory.*;
import static org.junit.jupiter.api.Assertions.*;

class SeatMapperTest {

    private SeatMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new SeatMapperImpl();
    }

    @Test
    void toEntity_shouldMapFieldsCorrectly() {
        // Arrange
        SeatRequestDTO dto = aSeatRequest();
        Flight flight = aFlight();

        // Act
        Seat seat = mapper.toEntity(dto, flight);

        // Assert
        assertAll("Seat entity mapping from SeatRequestDTO + Flight",
                () -> assertNotNull(seat, "Seat should not be null"),
                () -> assertNull(seat.getId(), "Id should be ignored when mapping"),
                () -> assertEquals("12A", seat.getSeatNumber(), "Seat number should come from DTO"),
                () -> assertEquals(SeatClass.BUSINESS, seat.getSeatClass(), "Seat class should come from DTO"),
                () -> assertEquals(flight, seat.getFlight(), "Flight should be set from method argument"),
                () -> assertEquals(Boolean.TRUE, seat.getIsAvailable(), "Availability should come from DTO"),
                () -> assertTrue(seat.getIsAvailable(), "Seat should be available"),
                () -> assertNull(seat.getCreatedAt(), "createdAt should be null on mapping"),
                () -> assertNull(seat.getUpdatedAt(), "updatedAt should be null on mapping")
        );
    }

    @Test
    void toResponse_shouldMapFieldsCorrectly() {
        // Arrange
        Seat seat = aSeat();

        // Act
        SeatResponseDTO response = mapper.toResponse(seat);

        // Assert
        assertAll("SeatResponseDTO mapping from Seat",
                () -> assertNotNull(response, "SeatResponseDTO should not be null"),
                () -> assertEquals(1L, response.id(), "Id should be mapped from Seat.id"),
                () -> assertEquals("12A", response.seatNumber(), "Seat number should be mapped from Seat.seatNumber"),
                () -> assertEquals(SeatClass.BUSINESS, response.seatClass(), "Seat class should be mapped from Seat.seatClass"),
                () -> assertTrue(response.isAvailable(), "Seat should be available"),
                () -> assertEquals("RO123", response.flightNumber(), "Flight number should be mapped from Flight.flightNumber"),
                () -> assertNull(response.createdAt(), "createdAt should be null if not set on entity"),
                () -> assertNull(response.updatedAt(), "updatedAt should be null if not set on entity")
        );
    }

    @Test
    void toDto_shouldMapFieldsCorrectly() {
        // Arrange
        Seat seat = aSeat();

        // Act
        SeatDTO dto = mapper.toDto(seat);

        // Assert
        assertAll("SeatDTO mapping from Seat",
                () -> assertNotNull(dto, "SeatDTO should not be null"),
                () -> assertEquals(seat.getId(), dto.id(), "Id should be mapped from Seat.id"),
                () -> assertEquals(seat.getFlight().getId(), dto.flightId(), "flightId should be mapped from Flight.id"),
                () -> assertEquals(seat.getSeatNumber(), dto.seatNumber(), "Seat number should match"),
                () -> assertEquals(seat.getSeatClass(), dto.seatClass(), "Seat class should match"),
                () -> assertEquals(seat.getIsAvailable(), dto.isAvailable(), "Availability should match"),
                () -> assertTrue(dto.isAvailable(), "SeatDTO should indicate available")
        );
    }
}
