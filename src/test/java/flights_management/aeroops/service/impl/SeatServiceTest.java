package flights_management.aeroops.service.impl;

import flights_management.aeroops.dto.seat.SeatResponseDTO;
import flights_management.aeroops.entity.Flight;
import flights_management.aeroops.entity.Seat;
import flights_management.aeroops.enums.SeatClass;
import flights_management.aeroops.error.BusinessException;
import flights_management.aeroops.mapper.SeatMapper;
import flights_management.aeroops.repository.FlightRepository;
import flights_management.aeroops.repository.SeatRepository;
import flights_management.aeroops.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static flights_management.aeroops.util.SeatTestDataFactory.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SeatServiceTest {

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private SeatMapper seatMapper;

    @InjectMocks
    private SeatService seatService;

    private Seat seat;
    private Flight flight;
    private Flight cancelledFlight;
    private Flight departedFlight;
    private Flight newFlight;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        seat = createSeat();
        flight = createFlight();
        cancelledFlight = createCancelledFlight();
        departedFlight = createDepartedFlight();
        newFlight = createNewFlight();

        if(testInfo.getTags().contains("create") &&
                testInfo.getTags().contains("happy-path")){
            mockCreateSeatHappyPath();
        }

        if (testInfo.getTags().contains("update") &&
                testInfo.getTags().contains("happy-path")) {
            mockUpdateSeatHappyPath(newFlight);
        }
    }

    private void mockCreateSeatHappyPath(){
        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
        when(seatRepository.countByFlightId(1L)).thenReturn(0L);
        when(seatRepository.existsByFlightIdAndSeatNumber(1L, "12A")).thenReturn(false);
    }

    private void mockUpdateSeatHappyPath(Flight newFlight){
        when(seatRepository.findById(1L)).thenReturn(Optional.of(seat));
        when(ticketRepository.existsBySeatId(1L)).thenReturn(false);
        when(flightRepository.findById(2L)).thenReturn(Optional.of(newFlight));
        when(seatRepository.countByFlightId(2L)).thenReturn(0L);
        when(seatRepository.existsByFlightIdAndSeatNumberAndIdNot(2L, "10A", 1L)).thenReturn(false);
    }

    @Tag("create")
    @Tag("happy-path")
    @Test
    void createSeat_shouldCreateSeatAndReturnResponse() {
        // Arrange
        SeatResponseDTO response = new SeatResponseDTO(10L, "12A", SeatClass.BUSINESS, true, "RO123", null, null);

        when(seatMapper.toEntity(request, flight)).thenReturn(seat);
        when(seatRepository.save(seat)).thenReturn(seat);
        when(seatMapper.toResponse(seat)).thenReturn(response);

        // Act
        SeatResponseDTO result = seatService.createSeat(request);

        // Assert
        assertAll(
                "created seat response",
                () -> assertNotNull(result),
                () -> assertEquals(10L,result.id()),
                () -> assertEquals(SeatClass.BUSINESS,result.seatClass()),
                () -> assertEquals("12A",result.seatNumber())
        );


        verify(flightRepository).findById(1L);
        verify(seatRepository).countByFlightId(1L);
        verify(seatRepository).existsByFlightIdAndSeatNumber(1L, "12A");
        verify(seatMapper).toEntity(request, flight);
        verify(seatRepository).save(seat);
        verify(seatMapper).toResponse(seat);
    }

    @Tag("create")
    @Tag("validation")
    @Tag("happy-path")
    @Test
    void createSeat_shouldThrow_whenSeatAlreadyExistsForFlight() {
        // Arrange
        when(seatRepository.existsByFlightIdAndSeatNumber(1L, "12A")).thenReturn(true);

        // Act + Assert
        assertThrows(BusinessException.class, () -> seatService.createSeat(request));

        verify(flightRepository).findById(1L);
        verify(seatRepository).countByFlightId(1L);
        verify(seatRepository).existsByFlightIdAndSeatNumber(1L, "12A");
        verifyNoInteractions(seatMapper);
    }

    @Tag("create")
    @Tag("validation")
    @Test
    void createSeat_shouldThrow_whenFlightNotFound() {
        // Arrange
        when(flightRepository.findById(1L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(BusinessException.class, () -> seatService.createSeat(request));

        verify(flightRepository).findById(1L);
        verifyNoInteractions(seatMapper);
    }

    @Tag("create")
    @Tag("validation")
    @Tag("happy-path")
    @Test
    void createSeat_shouldThrow_whenAircraftCapacityReached() {
        // Arrange
        when(seatRepository.countByFlightId(1L)).thenReturn(100L);

        // Act + Assert
        assertThrows(BusinessException.class, () -> seatService.createSeat(request));

        verify(flightRepository).findById(1L);
        verify(seatRepository).countByFlightId(1L);
        verify(seatRepository).existsByFlightIdAndSeatNumber(1L, "12A");
        verify(seatRepository,never()).save(any());
        verifyNoInteractions(seatMapper);
    }

    @Tag("create")
    @Tag("validation")
    @Tag("happy-path")
    @Test
    void createSeat_shouldThrow_whenFlightIsCancelled() {
        // Arrange
        when(flightRepository.findById(1L)).thenReturn(Optional.of(cancelledFlight));

        // Act + Assert
        assertThrows(BusinessException.class, () -> seatService.createSeat(request));

        verify(flightRepository).findById(1L);
        verify(seatRepository).countByFlightId(1L);
        verify(seatRepository).existsByFlightIdAndSeatNumber(1L, "12A");
        verify(seatRepository, never()).save(any());
        verifyNoInteractions(seatMapper);
    }

    @Tag("create")
    @Tag("validation")
    @Tag("happy-path")
    @Test
    void createSeat_shouldThrow_whenFlightIsDeparted() {
        // Arrange
        when(flightRepository.findById(1L)).thenReturn(Optional.of(departedFlight));

        // Act + Assert
        assertThrows(BusinessException.class, () -> seatService.createSeat(request));

        verify(flightRepository).findById(1L);
        verify(seatRepository).countByFlightId(1L);
        verify(seatRepository).existsByFlightIdAndSeatNumber(1L, "12A");
        verify(seatRepository, never()).save(any());
        verifyNoInteractions(seatMapper);

    }

    @Tag("get")
    @Test
    void getAllSeats_ShouldReturnAllSeats() {
        // Arrange
        Seat seat1 = new Seat();
        seat1.setId(1L);
        Seat seat2 = new Seat();
        seat2.setId(2L);

        List<Seat> seats = List.of(seat1, seat2);

        SeatResponseDTO dto1 = new SeatResponseDTO(1L, "10A", SeatClass.BUSINESS, true, "RO123", Instant.now(), Instant.now());
        SeatResponseDTO dto2 = new SeatResponseDTO(2L, "10B", SeatClass.ECONOMY, true, "RO342", Instant.now(), Instant.now());

        when(seatRepository.findAll()).thenReturn(seats);
        when(seatMapper.toResponse(seat1)).thenReturn(dto1);
        when(seatMapper.toResponse(seat2)).thenReturn(dto2);

        // Act
        List<SeatResponseDTO> result = seatService.getAllSeats();

        // Assert
        assertAll(
                "all seats",
                () -> assertEquals(2, result.size()),
                () -> assertEquals(1L, result.getFirst().id()),
                () -> assertEquals(2L, result.get(1).id())
        );

        verify(seatRepository).findAll();
        verify(seatMapper).toResponse(seat1);
        verify(seatMapper).toResponse(seat2);
        verifyNoMoreInteractions(seatRepository, seatMapper);
    }

    @Tag("get")
    @Test
    void getAllSeats_shouldReturnEmptyListWhenNoSeatsFound() {
        // Arrange
        when(seatRepository.findAll()).thenReturn(List.of());

        // Act
        List<SeatResponseDTO> result = seatService.getAllSeats();

        // Assert
        assertTrue(result.isEmpty());

        verify(seatRepository).findAll();
        verifyNoMoreInteractions(seatRepository);
        verifyNoInteractions(seatMapper);
    }

    @Tag("update")
    @Tag("happy-path")
    @Test
    void updateSeat_shouldUpdateSeatAndReturnResponse() {
        // Arrange
        SeatResponseDTO response = new SeatResponseDTO(1L, "10A", SeatClass.ECONOMY, false, "RO342", null, null);

        when(seatRepository.save(seat)).thenReturn(seat);
        when(seatMapper.toResponse(seat)).thenReturn(response);

        // Act
        SeatResponseDTO result = seatService.updateSeat(1L, requestWithNewFlight);

        // Assert
        assertAll(
                "updated seat response",
                () -> assertNotNull(result),
                () -> assertEquals(1L, result.id()),
                () -> assertEquals("10A", result.seatNumber()),
                () -> assertEquals(SeatClass.ECONOMY, result.seatClass()),
                () -> assertEquals("RO342", result.flightNumber()),
                () -> assertFalse(result.isAvailable())
        );

        assertAll(
                "updated seat entity",
                () -> assertEquals(newFlight, seat.getFlight()),
                () -> assertEquals("10A", seat.getSeatNumber()),
                () -> assertEquals(SeatClass.ECONOMY, seat.getSeatClass()),
                () -> assertFalse(seat.getIsAvailable())
        );

        verify(seatRepository).findById(1L);
        verify(ticketRepository).existsBySeatId(1L);
        verify(flightRepository).findById(2L);
        verify(seatRepository).countByFlightId(2L);
        verify(seatRepository).existsByFlightIdAndSeatNumberAndIdNot(2L, "10A", 1L);
        verify(seatRepository).save(seat);
        verify(seatMapper).toResponse(seat);
        verifyNoMoreInteractions(seatRepository, flightRepository, seatMapper, ticketRepository);
    }

    @Tag("update")
    @Tag("validation")
    @Test
    void updateSeat_shouldThrow_whenSeatNotFound() {
        // Arrange
        when(seatRepository.findById(999L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(BusinessException.class, () -> seatService.updateSeat(999L, request));

        verify(seatRepository).findById(999L);
        verifyNoInteractions(seatMapper, flightRepository, ticketRepository);
        verifyNoMoreInteractions(seatRepository);
    }

    @Tag("update")
    @Tag("validation")
    @Test
    void updateSeat_shouldThrow_whenNewFlightNotFound() {
        // Arrange
        when(seatRepository.findById(1L)).thenReturn(Optional.of(seat));
        when(ticketRepository.existsBySeatId(1L)).thenReturn(false);
        when(flightRepository.findById(2L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(BusinessException.class, () -> seatService.updateSeat(1L, requestWithNewFlight));

        verify(seatRepository).findById(1L);
        verify(ticketRepository).existsBySeatId(1L);
        verify(flightRepository).findById(2L);
        verifyNoMoreInteractions(seatRepository, flightRepository, ticketRepository);
        verifyNoInteractions(seatMapper);
    }

    @Tag("update")
    @Tag("validation")
    @Tag("happy-path")
    @Test
    void updateSeat_shouldThrow_whenSeatHasTicketsAndFlightChanges() {
        // Arrange
        when(ticketRepository.existsBySeatId(1L)).thenReturn(true);

        // Act + Assert
        assertThrows(BusinessException.class, () -> seatService.updateSeat(1L, requestWithNewFlight));

        verify(seatRepository).findById(1L);
        verify(ticketRepository).existsBySeatId(1L);
        verify(flightRepository).findById(2L);
        verifyNoMoreInteractions(seatRepository, seatMapper);
    }

    @Tag("update")
    @Tag("validation")
    @Tag("happy-path")
    @Test
    void updateSeat_shouldThrow_whenNewFlightCapacityReached() {
        // Arrange
        when(seatRepository.countByFlightId(2L)).thenReturn(150L);

        // Act + Assert
        assertThrows(BusinessException.class,
                () -> seatService.updateSeat(1L, requestWithNewFlight));

        verify(seatRepository).countByFlightId(2L);
    }

    @Tag("update")
    @Tag("validation")
    @Test
    void updateSeat_shouldThrow_whenCurrentFlightDeparted() {
        seat.setFlight(departedFlight);

        when(seatRepository.findById(1L)).thenReturn(Optional.of(seat));
        when(ticketRepository.existsBySeatId(1L)).thenReturn(false);
        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
        when(seatRepository.existsByFlightIdAndSeatNumberAndIdNot(1L, "12A", 1L)).thenReturn(false);

        // Act + Assert
        assertThrows(BusinessException.class, () -> seatService.updateSeat(1L, request));
    }

    @Tag("update")
    @Tag("validation")
    @Test
    void updateSeat_shouldThrow_whenSeatNumberAlreadyExistsForFlight() {
        // Arrange
        when(seatRepository.findById(1L)).thenReturn(Optional.of(seat));
        when(ticketRepository.existsBySeatId(1L)).thenReturn(false);
        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
        when(seatRepository.existsByFlightIdAndSeatNumberAndIdNot(1L, "12A", 1L)).thenReturn(true);

        // Act + Assert
        assertThrows(BusinessException.class, () -> seatService.updateSeat(1L, request));
    }

    @Tag("update")
    @Tag("validation")
    @Test
    void updateSeat_shouldThrow_whenNewFlightIsCancelled() {
        // Arrange
        when(seatRepository.findById(1L)).thenReturn(Optional.of(seat));
        when(ticketRepository.existsBySeatId(1L)).thenReturn(false);
        when(flightRepository.findById(1L)).thenReturn(Optional.of(cancelledFlight));
        when(seatRepository.existsByFlightIdAndSeatNumberAndIdNot(1L, "12A", 1L)).thenReturn(false);

        // Act + Assert
        assertThrows(BusinessException.class, () -> seatService.updateSeat(1L, request));

        verify(seatRepository).findById(1L);
        verify(ticketRepository).existsBySeatId(1L);
        verify(flightRepository).findById(1L);
        verify(seatRepository).existsByFlightIdAndSeatNumberAndIdNot(1L, "12A", 1L);
        verify(seatRepository, never()).save(any());
    }

    @Tag("update")
    @Tag("validation")
    @Test
    void updateSeat_shouldThrow_whenChangingToDepartedFlight() {
        // Arrange
        mockUpdateSeatHappyPath(departedFlight);

        // Act + Assert
        assertThrows(BusinessException.class, () -> seatService.updateSeat(1L, requestWithNewFlight));

        verify(seatRepository).findById(1L);
        verify(ticketRepository).existsBySeatId(1L);
        verify(flightRepository).findById(2L);
        verify(seatRepository).countByFlightId(2L);
        verify(seatRepository).existsByFlightIdAndSeatNumberAndIdNot(2L, "10A", 1L);
        verify(seatRepository, never()).save(any());
    }

    @Tag("delete")
    @Tag("happy-path")
    @Test
    void deleteSeat_shouldDeleteSeat() {
        // Arrange
        when(seatRepository.findById(1L)).thenReturn(Optional.of(seat));
        when(ticketRepository.existsBySeatId(1L)).thenReturn(false);

        // Act
        seatService.deleteSeat(1L);

        // Assert
        verify(seatRepository).findById(1L);
        verify(ticketRepository).existsBySeatId(1L);
        verify(seatRepository).delete(seat);
        verifyNoMoreInteractions(seatRepository);
    }

    @Tag("delete")
    @Tag("validation")
    @Test
    void deleteSeat_shouldThrow_whenSeatNotFound() {
        // Arrange
        when(seatRepository.findById(1L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(BusinessException.class, () -> seatService.deleteSeat(1L));

        verify(seatRepository).findById(1L);
        verify(seatRepository, never()).delete(any());
        verifyNoMoreInteractions(seatRepository);
        verifyNoInteractions(ticketRepository);
    }

    @Tag("delete")
    @Tag("validation")
    @Test
    void deleteSeat_shouldThrow_whenSeatHasTickets() {
        // Arrange
        when(seatRepository.findById(1L)).thenReturn(Optional.of(seat));
        when(ticketRepository.existsBySeatId(1L)).thenReturn(true);

        // Act + Assert
        assertThrows(BusinessException.class, () -> seatService.deleteSeat(1L));

        verify(seatRepository).findById(1L);
        verify(ticketRepository).existsBySeatId(1L);
        verify(seatRepository, never()).delete(any());
    }

    @Tag("delete")
    @Tag("validation")
    @Test
    void deleteSeat_shouldThrow_whenFlightDeparted() {
        // Arrange
        seat.setFlight(departedFlight);
        when(seatRepository.findById(1L)).thenReturn(Optional.of(seat));
        when(ticketRepository.existsBySeatId(1L)).thenReturn(false);

        // Act + Assert
        assertThrows(BusinessException.class, () -> seatService.deleteSeat(1L));

        verify(seatRepository).findById(1L);
        verify(ticketRepository).existsBySeatId(1L);
        verify(seatRepository, never()).delete(any());
    }
}
