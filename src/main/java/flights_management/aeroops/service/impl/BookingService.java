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

    @Override
    public BookingResponseDTO createBooking(BookingRequestDTO bookingRequestDTO) {
        List<ErrorModel> errors = new ArrayList<>();

        Flight flight = flightsRepository.findById(bookingRequestDTO.flightId()).orElse(null);
        if (flight == null) {
            errors.add(new ErrorModel("FLIGHT_NOT_FOUND", "Flight not found"));
        }
        Passenger passenger = passengerRepository.findById(bookingRequestDTO.passengerId()).orElse(null);
        if (passenger == null) {
            errors.add(new ErrorModel("PASSENGER_NOT_FOUND", "Passenger not found"));
        }
        if (flight != null && flight.getStatus() == Status.CANCELLED) {
            errors.add(new ErrorModel("FLIGHT_CANCELLED", "Cannot create booking on a cancelled flight"));
        }

        if (!errors.isEmpty()) {
            throw new BusinessException(errors);
        }

        //  DTO -> enitity
        Booking booking = bookingMapper.toEntity(bookingRequestDTO, flight, passenger);

        // persist & response
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

}
