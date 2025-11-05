package flights_management.aeroops.service;

import flights_management.aeroops.dto.booking.BookingRequestDTO;
import flights_management.aeroops.dto.booking.BookingResponseDTO;
import flights_management.aeroops.dto.flight.FlightRequestDTO;
import flights_management.aeroops.dto.flight.FlightResponseDTO;

import java.util.List;

public interface IBookingService {
    BookingResponseDTO createBooking(BookingRequestDTO bookingRequestDTO);
    List<BookingResponseDTO> getAllBookings();
}
