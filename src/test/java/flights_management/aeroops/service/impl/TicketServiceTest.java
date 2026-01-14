package flights_management.aeroops.service.impl;

import flights_management.aeroops.dto.ticket.TicketRequestDTO;
import flights_management.aeroops.dto.ticket.TicketResponseDTO;
import flights_management.aeroops.entity.Booking;
import flights_management.aeroops.entity.Flight;
import flights_management.aeroops.entity.Seat;
import flights_management.aeroops.entity.Ticket;
import flights_management.aeroops.enums.BookingStatus;
import flights_management.aeroops.enums.SeatClass;
import flights_management.aeroops.error.BusinessException;
import flights_management.aeroops.mapper.TicketMapper;
import flights_management.aeroops.repository.BookingRepository;
import flights_management.aeroops.repository.SeatRepository;
import flights_management.aeroops.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static flights_management.aeroops.util.SeatTestDataFactory.*;
import static flights_management.aeroops.util.TicketTestDataFactory.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private TicketMapper ticketMapper;

    @InjectMocks
    TicketService ticketService;

    private Booking booking;
    private Seat seat;
    private Ticket ticket;
    private TicketRequestDTO request;

    @BeforeEach
    void setUp() {
        booking = aBooking();
        seat = aSeat();
        ticket = aTicket(booking, seat, BigDecimal.valueOf(100));
        request = aTicketRequest();
    }

    private void mockBookingAndSeatFound() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(seatRepository.findById(1L)).thenReturn(Optional.of(seat));
    }

    private void mockSeatFree() {
        when(ticketRepository.existsBySeat(seat)).thenReturn(false);
    }

    // ~~~~~~~~~~~~~~~~ CREATE ~~~~~~~~~~~~~~~~

    @Test
    void createTicket_shouldCreateTicketAndUpdateSeatAndBooking() {
        // Arrange
        TicketResponseDTO response = new TicketResponseDTO(
                1L,
                "123",
                BookingStatus.HOLD,
                BigDecimal.valueOf(100),
                "12A",
                SeatClass.BUSINESS,
                null, null
        );

        mockBookingAndSeatFound();
        mockSeatFree();

        when(ticketMapper.toEntity(request, booking, seat)).thenReturn(ticket);
        when(ticketRepository.save(ticket)).thenReturn(ticket);
        when(ticketMapper.toResponse(ticket)).thenReturn(response);

        // Act
        TicketResponseDTO result = ticketService.createTicket(request);

        // Assert DTO
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(1L, result.id()),
                () -> assertEquals("123", result.code()),
                () -> assertEquals(BookingStatus.HOLD, result.bookingStatus()),
                () -> assertEquals(BigDecimal.valueOf(100), result.price()),
                () -> assertEquals("12A", result.seatNumber()),
                () -> assertEquals(SeatClass.BUSINESS, result.seatClass())
        );

        // Assert side-effects
        assertAll(
                () -> assertFalse(seat.getIsAvailable()),
                () -> assertEquals(BigDecimal.valueOf(300), booking.getPriceTotal())
        );

        verify(bookingRepository).findById(1L);
        verify(seatRepository).findById(1L);
        verify(ticketRepository).existsBySeat(seat);
        verify(ticketRepository).save(ticket);
        verify(seatRepository).save(seat);
        verify(bookingRepository).save(booking);
        verify(ticketMapper).toEntity(request, booking, seat);
        verify(ticketMapper).toResponse(ticket);
        verifyNoMoreInteractions(ticketRepository, bookingRepository, seatRepository, ticketMapper);
    }

    @Test
    void createTicket_shouldThrow_whenBookingIsNull() {
        // Arrange
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());
        when(seatRepository.findById(1L)).thenReturn(Optional.of(seat));

        // Act + Assert
        assertThrows(BusinessException.class, () -> ticketService.createTicket(request));

        verify(bookingRepository).findById(1L);
        verify(seatRepository).findById(1L);
        verifyNoInteractions(ticketMapper, ticketRepository);
        verifyNoMoreInteractions(bookingRepository, seatRepository);
    }

    @Test
    void createTicket_shouldThrow_whenSeatIsNull() {
        // Arrange
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(seatRepository.findById(1L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(BusinessException.class, () -> ticketService.createTicket(request));

        verify(bookingRepository).findById(1L);
        verify(seatRepository).findById(1L);
        verifyNoInteractions(ticketMapper, ticketRepository);
        verifyNoMoreInteractions(bookingRepository, seatRepository);
    }

    @Test
    void createTicket_shouldThrow_whenSeatIsNotOnTheSameFlightWithBookingFlight() {
        // Arrange
        Booking bookingOnAnotherFlight = createBookingOnFlight(aNewFlight(), 2L);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(bookingOnAnotherFlight));
        when(seatRepository.findById(1L)).thenReturn(Optional.of(seat));
        when(ticketRepository.existsBySeat(seat)).thenReturn(false);

        // Act + Assert
        assertThrows(BusinessException.class, () -> ticketService.createTicket(request));

        verify(bookingRepository).findById(1L);
        verify(seatRepository).findById(1L);
        verify(ticketRepository).existsBySeat(seat);
        verifyNoInteractions(ticketMapper);
        verifyNoMoreInteractions(bookingRepository, seatRepository, ticketMapper);
    }

    @Test
    void createTicket_shouldThrow_whenBookingStatusIsTerminal() {
        // Arrange
        booking.setStatus(BookingStatus.CANCELLED);
        mockBookingAndSeatFound();
        mockSeatFree();

        // Act + Assert
        assertThrows(BusinessException.class, () -> ticketService.createTicket(request));

        verify(bookingRepository).findById(1L);
        verify(seatRepository).findById(1L);
        verify(ticketRepository).existsBySeat(seat);
        verifyNoInteractions(ticketMapper);
        verifyNoMoreInteractions(bookingRepository, seatRepository, ticketRepository);
    }

    @Test
    void createTicket_shouldThrow_whenFlightIsCancelled() {
        // Arrange
        booking.setStatus(BookingStatus.HOLD);
        Flight cancelledFlight = aCancelledFlight();
        booking.setFlight(cancelledFlight);
        seat.setFlight(cancelledFlight);

        mockBookingAndSeatFound();
        mockSeatFree();

        // Act + Assert
        assertThrows(BusinessException.class, () -> ticketService.createTicket(request));

        verify(bookingRepository).findById(1L);
        verify(seatRepository).findById(1L);
        verify(ticketRepository).existsBySeat(seat);
        verifyNoInteractions(ticketMapper);
        verifyNoMoreInteractions(bookingRepository, seatRepository, ticketRepository);
    }

    @Test
    void createTicket_shouldThrow_whenFlightIsDeparted() {
        // Arrange
        booking.setStatus(BookingStatus.HOLD);
        Flight departedFlight = aDepartedFlight();
        booking.setFlight(departedFlight);
        seat.setFlight(departedFlight);

        mockBookingAndSeatFound();
        mockSeatFree();

        // Act + Assert
        assertThrows(BusinessException.class, () -> ticketService.createTicket(request));

        verify(bookingRepository).findById(1L);
        verify(seatRepository).findById(1L);
        verify(ticketRepository).existsBySeat(seat);
        verifyNoInteractions(ticketMapper);
        verifyNoMoreInteractions(bookingRepository, seatRepository, ticketRepository);
    }

    @Test
    void createTicket_shouldThrow_whenSeatIsAlreadyAllocated() {
        // Arrange
        mockBookingAndSeatFound();
        when(ticketRepository.existsBySeat(seat)).thenReturn(true);

        // Act + Assert
        assertThrows(BusinessException.class, () -> ticketService.createTicket(request));

        verify(bookingRepository).findById(1L);
        verify(seatRepository).findById(1L);
        verify(ticketRepository).existsBySeat(seat);
        verifyNoInteractions(ticketMapper);
        verifyNoMoreInteractions(bookingRepository, seatRepository, ticketRepository);
    }

    @Test
    void createTicket_shouldThrow_whenSeatIsNotAvailable() {
        // Arrange
        seat.setIsAvailable(false);
        mockBookingAndSeatFound();
        mockSeatFree();

        // Act + Assert
        assertThrows(BusinessException.class, () -> ticketService.createTicket(request));

        verify(bookingRepository).findById(1L);
        verify(seatRepository).findById(1L);
        verify(ticketRepository).existsBySeat(seat);
        verifyNoInteractions(ticketMapper);
        verifyNoMoreInteractions(bookingRepository, seatRepository, ticketRepository);
    }

    @Test
    void createTicket_shouldThrow_whenPriceIsZero() {
        // Arrange
        TicketRequestDTO invalidRequest = aTicketRequestWithPrice(BigDecimal.ZERO);
        mockBookingAndSeatFound();
        mockSeatFree();

        // Act + Assert
        assertThrows(BusinessException.class, () -> ticketService.createTicket(invalidRequest));

        verify(bookingRepository).findById(1L);
        verify(seatRepository).findById(1L);
        verify(ticketRepository).existsBySeat(seat);
        verifyNoInteractions(ticketMapper);
        verifyNoMoreInteractions(bookingRepository, seatRepository, ticketRepository);
    }

    @Test
    void createTicket_shouldThrow_whenPriceIsNull() {
        // Arrange
        TicketRequestDTO invalidRequest = new TicketRequestDTO(1L, 1L, null);
        mockBookingAndSeatFound();
        mockSeatFree();

        // Act + Assert
        assertThrows(BusinessException.class, () -> ticketService.createTicket(invalidRequest));

        verify(bookingRepository).findById(1L);
        verify(seatRepository).findById(1L);
        verify(ticketRepository).existsBySeat(seat);
        verifyNoInteractions(ticketMapper);
        verifyNoMoreInteractions(bookingRepository, seatRepository, ticketRepository);
    }

    // ~~~~~~~~~~~~~~~~ GET ~~~~~~~~~~~~~~~~

    @Test
    void getAllTickets_shouldReturnAllTickets() {
        // Arrange
        Booking booking2 = aBooking();
        booking2.setId(2L);
        Seat seat2 = aSeat();
        seat2.setId(2L);
        Ticket ticket1 = aTicket(booking, seat);
        Ticket ticket2 = aTicket(booking2, seat2);
        ticket2.setId(2L);

        TicketResponseDTO response1 = new TicketResponseDTO(
                1L, "123", BookingStatus.HOLD,
                BigDecimal.valueOf(100),
                "12A", SeatClass.BUSINESS,
                null, null
        );

        TicketResponseDTO response2 = new TicketResponseDTO(
                2L, "456", BookingStatus.CONFIRMED,
                BigDecimal.valueOf(200),
                "14C", SeatClass.ECONOMY,
                null, null
        );

        when(ticketRepository.findAll()).thenReturn(List.of(ticket1, ticket2));
        when(ticketMapper.toResponse(ticket1)).thenReturn(response1);
        when(ticketMapper.toResponse(ticket2)).thenReturn(response2);

        // Act
        List<TicketResponseDTO> result = ticketService.getAllTickets();

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(2, result.size()),
                () -> assertEquals(1L, result.getFirst().id()),
                () -> assertEquals(2L, result.get(1).id())
        );

        verify(ticketRepository).findAll();
        verify(ticketMapper).toResponse(ticket1);
        verify(ticketMapper).toResponse(ticket2);
        verifyNoMoreInteractions(ticketRepository, ticketMapper);
    }

    @Test
    void getAllTickets_shouldReturnEmptyListWhenNoTicketsFound() {
        // Arrange
        when(ticketRepository.findAll()).thenReturn(List.of());

        // Act
        List<TicketResponseDTO> result = ticketService.getAllTickets();

        // Assert
        assertAll(
            () -> assertNotNull(result),
            () -> assertTrue(result.isEmpty())
        );

        verify(ticketRepository).findAll();
        verifyNoInteractions(ticketMapper);
        verifyNoMoreInteractions(ticketRepository);
    }

    // ~~~~~~~~~~~~~~~~ UPDATE ~~~~~~~~~~~~~~~~

    @Test
    void updateTicket_shouldThrowWhenTicketNotFound() {
        // Arrange
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(BusinessException.class, () -> ticketService.updateTicket(1L, request));

        verify(ticketRepository).findById(1L);
        verifyNoMoreInteractions(ticketRepository);
        verifyNoInteractions(bookingRepository, seatRepository, ticketMapper);
    }

    @Test
    void updateTicket_shouldUpdatePriceAndBookingTotal_whenSeatAndBookingStaySame() {
        // Arrange
        BigDecimal newPrice = BigDecimal.valueOf(150);
        TicketRequestDTO updateRequest = new TicketRequestDTO(booking.getId(), seat.getId(), newPrice);

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(seatRepository.findById(seat.getId())).thenReturn(Optional.of(seat));
        when(ticketRepository.existsBySeatAndIdNot(seat, 1L)).thenReturn(false);

        TicketResponseDTO response = new TicketResponseDTO(
                ticket.getId(),
                booking.getCode(),
                booking.getStatus(),
                newPrice,
                seat.getSeatNumber(),
                seat.getSeatClass(),
                null, null
        );
        when(ticketMapper.toResponse(ticket)).thenReturn(response);

        // Act
        TicketResponseDTO result = ticketService.updateTicket(1L, updateRequest);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(newPrice, result.price()),
                () -> assertEquals(newPrice, ticket.getPrice()),
                () -> assertEquals(BigDecimal.valueOf(250), booking.getPriceTotal()),
                () -> assertSame(seat, ticket.getSeat())
        );

        verify(ticketRepository).findById(1L);
        verify(bookingRepository).findById(booking.getId());
        verify(seatRepository).findById(seat.getId());
        verify(ticketRepository).existsBySeatAndIdNot(seat, 1L);
        verify(ticketRepository).save(ticket);
        verify(bookingRepository).save(booking);
        verify(ticketMapper).toResponse(ticket);
        verifyNoMoreInteractions(ticketRepository, bookingRepository, seatRepository, ticketMapper);
    }

    @Test
    void updateTicket_shouldMoveSeatAndUpdateAvailability_whenSeatChangesOnSameBooking() {
        // Arrange
        seat.setIsAvailable(false);

        Seat newSeat = aSeat();
        newSeat.setId(2L);
        newSeat.setFlight(seat.getFlight());
        newSeat.setIsAvailable(true);

        BigDecimal newPrice = BigDecimal.valueOf(150);
        TicketRequestDTO updateRequest = new TicketRequestDTO(booking.getId(), newSeat.getId(), newPrice);

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(seatRepository.findById(newSeat.getId())).thenReturn(Optional.of(newSeat));
        when(ticketRepository.existsBySeatAndIdNot(newSeat, 1L)).thenReturn(false);
        when(ticketRepository.existsBySeat(seat)).thenReturn(false);

        TicketResponseDTO response = new TicketResponseDTO(
                ticket.getId(),
                booking.getCode(),
                booking.getStatus(),
                newPrice,
                newSeat.getSeatNumber(),
                newSeat.getSeatClass(),
                null, null
        );
        when(ticketMapper.toResponse(ticket)).thenReturn(response);

        // Act
        TicketResponseDTO result = ticketService.updateTicket(1L, updateRequest);

        // Assert
        assertAll(
            () -> assertNotNull(result),
            () -> assertEquals(newPrice, result.price()),
            () -> assertSame(newSeat, ticket.getSeat()),
            () -> assertTrue(seat.getIsAvailable()),
            () -> assertFalse(newSeat.getIsAvailable()),
            () -> assertEquals(BigDecimal.valueOf(250), booking.getPriceTotal())
        );

        verify(ticketRepository).findById(1L);
        verify(bookingRepository).findById(booking.getId());
        verify(seatRepository).findById(newSeat.getId());
        verify(ticketRepository).existsBySeatAndIdNot(newSeat, 1L);
        verify(ticketRepository).existsBySeat(seat);
        verify(seatRepository).save(seat);
        verify(seatRepository).save(newSeat);
        verify(bookingRepository).save(booking);
        verify(ticketRepository).save(ticket);
        verify(ticketMapper).toResponse(ticket);
        verifyNoMoreInteractions(ticketRepository, bookingRepository, seatRepository, ticketMapper);
    }

    @Test
    void updateTicket_shouldMoveTicketToAnotherBookingAndUpdateBothTotals() {
        // Arrange
        Booking newBooking = aBooking();
        newBooking.setId(2L);
        newBooking.setFlight(booking.getFlight());
        newBooking.setPriceTotal(BigDecimal.valueOf(50));

        BigDecimal newPrice = BigDecimal.valueOf(150);
        TicketRequestDTO updateRequest = new TicketRequestDTO(newBooking.getId(), seat.getId(), newPrice);

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(bookingRepository.findById(newBooking.getId())).thenReturn(Optional.of(newBooking));
        when(seatRepository.findById(seat.getId())).thenReturn(Optional.of(seat));
        when(ticketRepository.existsBySeatAndIdNot(seat, 1L)).thenReturn(false);

        TicketResponseDTO response = new TicketResponseDTO(
                ticket.getId(),
                newBooking.getCode(),
                newBooking.getStatus(),
                newPrice,
                seat.getSeatNumber(),
                seat.getSeatClass(),
                null, null
        );
        when(ticketMapper.toResponse(ticket)).thenReturn(response);

        // Act
        TicketResponseDTO result = ticketService.updateTicket(1L, updateRequest);

        // Assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertSame(newBooking, ticket.getBooking()),
                () -> assertEquals(BigDecimal.valueOf(100), booking.getPriceTotal()),
                () -> assertEquals(BigDecimal.valueOf(200), newBooking.getPriceTotal())
        );

        verify(ticketRepository).findById(1L);
        verify(bookingRepository).findById(newBooking.getId());
        verify(seatRepository).findById(seat.getId());
        verify(ticketRepository).existsBySeatAndIdNot(seat, 1L);
        verify(bookingRepository).save(booking);
        verify(bookingRepository).save(newBooking);
        verify(ticketRepository).save(ticket);
        verify(ticketMapper).toResponse(ticket);
        verifyNoMoreInteractions(seatRepository);
    }

    @Test
    void updateTicket_shouldThrowWhenNewSeatIsUsedByAnotherTicket() {
        // Arrange
        Seat newSeat = aSeat();
        newSeat.setId(2L);
        newSeat.setFlight(seat.getFlight());

        TicketRequestDTO updateRequest = new TicketRequestDTO(
                booking.getId(), newSeat.getId(), BigDecimal.valueOf(150)
        );

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));
        when(bookingRepository.findById(booking.getId())).thenReturn(Optional.of(booking));
        when(seatRepository.findById(newSeat.getId())).thenReturn(Optional.of(newSeat));
        when(ticketRepository.existsBySeatAndIdNot(newSeat, 1L)).thenReturn(true);

        // Act + Assert
        assertThrows(BusinessException.class, () -> ticketService.updateTicket(1L, updateRequest));

        verify(ticketRepository).findById(1L);
        verify(bookingRepository).findById(booking.getId());
        verify(seatRepository).findById(newSeat.getId());
        verify(ticketRepository).existsBySeatAndIdNot(newSeat, 1L);
        verifyNoMoreInteractions(ticketRepository);
        verifyNoInteractions(ticketMapper);
        verifyNoMoreInteractions(bookingRepository, seatRepository);
    }

    // ~~~~~~~~~~~~~~~~ DELETE ~~~~~~~~~~~~~~~~

    @Test
    void deleteTicket_shouldThrow_whenTicketNotFound() {
        // Arrange
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(BusinessException.class, () -> ticketService.deleteTicket(1L));

        verify(ticketRepository).findById(1L);
        verifyNoMoreInteractions(ticketRepository);
        verifyNoInteractions(bookingRepository, seatRepository, ticketMapper);
    }

    @Test
    void deleteTicket_shouldDeleteTicket_FreeSeatAndUpdateBookingPrice_whenNoOtherTicketsOnSeat() {
        // Arrange
        Ticket ticketToDelete = ticket;
        Booking bookingForTicket = ticketToDelete.getBooking();
        Seat seatForTicket = ticketToDelete.getSeat();

        bookingForTicket.setPriceTotal(BigDecimal.valueOf(300));
        ticketToDelete.setPrice(BigDecimal.valueOf(100));

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticketToDelete));
        when(ticketRepository.existsBySeat(seatForTicket)).thenReturn(false);

        // Act
        ticketService.deleteTicket(1L);

        // Assert
        assertAll(
                () -> assertEquals(BigDecimal.valueOf(200), bookingForTicket.getPriceTotal()),
                () -> assertTrue(seatForTicket.getIsAvailable())
        );

        verify(ticketRepository).findById(1L);
        verify(ticketRepository).delete(ticketToDelete);
        verify(ticketRepository).existsBySeat(seatForTicket);
        verify(seatRepository).save(seatForTicket);
        verify(bookingRepository).save(bookingForTicket);
        verifyNoInteractions(ticketMapper);
        verifyNoMoreInteractions(ticketRepository, seatRepository, bookingRepository);
    }

    @Test
    void deleteTicket_shouldDeleteTicket_KeepSeatUnavailableAndUpdateBookingPrice_whenOtherTicketsExistOnSeat() {
        // Arrange
        Ticket ticketToDelete = ticket;
        Booking bookingForTicket = ticketToDelete.getBooking();
        Seat seatForTicket = ticketToDelete.getSeat();

        bookingForTicket.setPriceTotal(BigDecimal.valueOf(300));
        ticketToDelete.setPrice(BigDecimal.valueOf(100));
        seatForTicket.setIsAvailable(false);

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticketToDelete));
        when(ticketRepository.existsBySeat(seatForTicket)).thenReturn(true);

        // Act
        ticketService.deleteTicket(1L);

        // Assert
        assertAll(
                () -> assertEquals(BigDecimal.valueOf(200), bookingForTicket.getPriceTotal()),
                () -> assertFalse(seatForTicket.getIsAvailable())
        );

        verify(ticketRepository).findById(1L);
        verify(ticketRepository).delete(ticketToDelete);
        verify(ticketRepository).existsBySeat(seatForTicket);
        verify(bookingRepository).save(bookingForTicket);
        verifyNoMoreInteractions(seatRepository);
        verifyNoMoreInteractions(ticketRepository, bookingRepository);
        verifyNoInteractions(ticketMapper);
    }
}
