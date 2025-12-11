package flights_management.aeroops.service.impl;

import flights_management.aeroops.dto.seat.SeatResponseDTO;
import flights_management.aeroops.entity.Seat;
import flights_management.aeroops.enums.SeatClass;
import flights_management.aeroops.mapper.SeatMapper;
import flights_management.aeroops.repository.SeatRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class SeatServiceTest {

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private SeatMapper seatMapper;

    @InjectMocks
    private SeatService seatService;

    @Test
    void createSeat() {
    }

    @Test
    void getAllSeats() {
        // Arrange
        Seat seat1 = new Seat();
        seat1.setId(1L);

        Seat seat2 = new Seat();
        seat2.setId(2L);

        List<Seat> seats = List.of(seat1, seat2);

        SeatResponseDTO dto1 = new SeatResponseDTO(
                1L, "10A", SeatClass.ECONOMY, true, "RO123",
                Instant.now(), Instant.now()
        );

        SeatResponseDTO dto2 = new SeatResponseDTO(
                2L, "10B", SeatClass.BUSINESS, true, "RO456",
                Instant.now(), Instant.now()
        );

        when(seatRepository.findAll()).thenReturn(seats);
        when(seatMapper.toResponse(seat1)).thenReturn(dto1);
        when(seatMapper.toResponse(seat2)).thenReturn(dto2);

        // Act
        List<SeatResponseDTO> result = seatService.getAllSeats();

        // Assert
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals(2L, result.get(1).id());

        verify(seatRepository, times(1)).findAll();
        verify(seatMapper, times(1)).toResponse(seat1);
        verify(seatMapper, times(1)).toResponse(seat2);
    }

    @Test
    void updateSeat() {
    }

    @Test
    void deleteSeat() {
    }
}