package flights_management.aeroops.mapper;

import flights_management.aeroops.dto.seat.SeatDTO;
import flights_management.aeroops.dto.seat.SeatRequestDTO;
import flights_management.aeroops.dto.seat.SeatResponseDTO;
import flights_management.aeroops.entity.Flight;
import flights_management.aeroops.entity.Seat;
import flights_management.aeroops.enums.SeatClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class SeatMapperTest {

    private SeatMapper mapper;

    @BeforeEach
    void setUp() {
        mapper =  new SeatMapperImpl();
    }

    @Test
    void toEntity() {
        // Arrange
        SeatRequestDTO dto = new SeatRequestDTO(10L,"12A", SeatClass.BUSINESS);

        Flight flight = new Flight();
        flight.setId(10L);

        // Act
        Seat seat = mapper.toEntity(dto, flight);

        // Assert
        assertNotNull(seat);
        assertEquals("12A",seat.getSeatNumber());
        assertEquals(SeatClass.BUSINESS,seat.getSeatClass());
        assertEquals(flight,seat.getFlight());
        assertTrue(seat.getIsAvailable());
        assertNull(seat.getId());
        assertNull(seat.getCreatedAt());
        assertNull(seat.getUpdatedAt());

    }

    @Test
    void toResponse() {
        // Arrange
        Seat seat = createMockSeat();

        // Act
        SeatResponseDTO seatResponseDTO = mapper.toResponse(seat);

        // Assert
        assertEquals(10L,seatResponseDTO.id());
        assertEquals("12A",seatResponseDTO.seatNumber());
        assertEquals(SeatClass.BUSINESS,seatResponseDTO.seatClass());
        assertTrue(seatResponseDTO.isAvailable());
        assertEquals("RO123",seatResponseDTO.flightNumber());
        assertNull(seatResponseDTO.createdAt());
        assertNull(seatResponseDTO.updatedAt());
    }

    @Test
    void toDto() {
        // Arrange
        Seat seat = createMockSeat();

        // Act
        SeatDTO seatDTO = mapper.toDto(seat);

        //Arrange
        assertEquals(10L,seatDTO.id());
        assertEquals(10L, seatDTO.flightId());
        assertEquals("12A",seatDTO.seatNumber());
        assertEquals(SeatClass.BUSINESS,seatDTO.seatClass());
        assertTrue(seatDTO.isAvailable());

    }

    private Seat createMockSeat(){
        Flight flight = new Flight();
        flight.setId(10L);
        flight.setFlightNumber("RO123");

        Seat seat = new Seat();
        seat.setId(10L);
        seat.setFlight(flight);
        seat.setSeatNumber("12A");
        seat.setSeatClass(SeatClass.BUSINESS);
        seat.setIsAvailable(true);

        return seat;
    }
}