package flights_management.aeroops.util;

import flights_management.aeroops.dto.ticket.TicketRequestDTO;
import flights_management.aeroops.entity.Booking;
import flights_management.aeroops.entity.Flight;
import flights_management.aeroops.entity.Seat;
import flights_management.aeroops.entity.Ticket;
import flights_management.aeroops.enums.BookingStatus;

import java.math.BigDecimal;

import static flights_management.aeroops.util.SeatTestDataFactory.aFlight;


public final class TicketTestDataFactory {

    private TicketTestDataFactory() {}

    // ~~~~~~~~~~~~~~~~ DTO ~~~~~~~~~~~~~~~~

    public static TicketRequestDTO aTicketRequest() {
        return new TicketRequestDTO(1L, 1L, BigDecimal.valueOf(100));
    }

    public static TicketRequestDTO aTicketRequestWithPrice(BigDecimal price) {
        return new TicketRequestDTO(1L, 1L, price);
    }

    // ~~~~~~~~~~~~~~~~ BOOKING ~~~~~~~~~~~~~~~~

    public static Booking aBooking() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setCode("123");
        booking.setFlight(aFlight());
        booking.setStatus(BookingStatus.HOLD);
        booking.setPriceTotal(BigDecimal.valueOf(200));
        return booking;
    }

    public static Booking createBookingOnFlight(Flight flight, Long id) {
        Booking booking = new Booking();
        booking.setId(id);
        booking.setCode("123");
        booking.setFlight(flight);
        booking.setStatus(BookingStatus.HOLD);
        booking.setPriceTotal(BigDecimal.valueOf(200));
        return booking;
    }

    // ~~~~~~~~~~~~~~~~ TICKET ~~~~~~~~~~~~~~~~

    public static Ticket aTicket(Booking booking, Seat seat) {
        return aTicket(booking, seat, BigDecimal.valueOf(100));
    }

    public static Ticket aTicket(Booking booking, Seat seat, BigDecimal price) {
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setBooking(booking);
        ticket.setSeat(seat);
        ticket.setPrice(price);
        return ticket;
    }
}
