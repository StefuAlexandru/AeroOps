package flights_management.aeroops.service.impl;

import flights_management.aeroops.dto.booking.BookingRequestDTO;
import flights_management.aeroops.dto.booking.BookingResponseDTO;
import flights_management.aeroops.entity.Booking;
import flights_management.aeroops.entity.Flight;
import flights_management.aeroops.entity.Passenger;
import flights_management.aeroops.enums.Status;
import flights_management.aeroops.error.BusinessException;
import flights_management.aeroops.error.ErrorModel;
import flights_management.aeroops.mapper.BookingMapper;
import flights_management.aeroops.repository.BookingRepository;
import flights_management.aeroops.repository.FlightRepository;
import flights_management.aeroops.repository.PassengerRepository;
import flights_management.aeroops.service.IBookingService;
import flights_management.aeroops.util.PnrGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingService implements IBookingService {

    private final BookingRepository bookingRepository;
    private final FlightRepository flightsRepository;
    private final PassengerRepository passengerRepository;
    private final BookingMapper bookingMapper;
    private final PnrGenerator pnrGenerator;


    @Override
    public BookingResponseDTO createBooking(BookingRequestDTO bookingRequestDTO) {
        ValidatedBookingData validated = validateAndFetch(bookingRequestDTO);

        Booking booking = bookingMapper.toEntity(
                bookingRequestDTO,
                validated.flight(),
                validated.passenger(),
                pnrGenerator
        );

        Booking saved = bookingRepository.save(booking);
        return bookingMapper.toResponse(saved);
    }


    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDTO> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(bookingMapper::toResponse)
                .toList();
    }


    @Override
    public BookingResponseDTO updateBooking(Long id, BookingRequestDTO requestDTO) {
        Booking bookingToUpdate = bookingRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        List.of(new ErrorModel("BOOKING_NOT_FOUND", "Booking not found"))
                ));

        ValidatedBookingData validated = validateAndFetch(requestDTO);

        bookingToUpdate.setFlight(validated.flight());
        bookingToUpdate.setPassenger(validated.passenger());
        bookingToUpdate.setPriceTotal(requestDTO.priceTotal());

        Booking updated = bookingRepository.save(bookingToUpdate);
        return bookingMapper.toResponse(updated);
    }


    @Override
    public void deleteBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        List.of(new ErrorModel("BOOKING_NOT_FOUND", "Booking not found"))
                ));
        bookingRepository.delete(booking);
    }


    private record ValidatedBookingData(Flight flight, Passenger passenger) { }

    private ValidatedBookingData validateAndFetch(BookingRequestDTO bookingRequestDTO) {
        List<ErrorModel> errors = new ArrayList<>();

        Flight flight = flightsRepository.findById(bookingRequestDTO.flightId()).orElse(null);
        if (flight == null) {
            errors.add(new ErrorModel("FLIGHT_NOT_FOUND", "Flight not found"));
        } else if (flight.getStatus() == Status.CANCELLED) {
            errors.add(new ErrorModel("FLIGHT_CANCELLED", "Cannot create/update booking on a cancelled flight"));
        }

        Passenger passenger = passengerRepository.findById(bookingRequestDTO.passengerId()).orElse(null);
        if (passenger == null) {
            errors.add(new ErrorModel("PASSENGER_NOT_FOUND", "Passenger not found"));
        }

        if (!errors.isEmpty()) {
            throw new BusinessException(errors);
        }

        return new ValidatedBookingData(flight, passenger);
    }
}
