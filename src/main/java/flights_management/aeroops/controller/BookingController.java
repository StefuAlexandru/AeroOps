package flights_management.aeroops.controller;

import flights_management.aeroops.dto.booking.BookingRequestDTO;
import flights_management.aeroops.dto.booking.BookingResponseDTO;
import flights_management.aeroops.service.IBookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final IBookingService bookingService;

    @PostMapping("/create")
    public ResponseEntity<BookingResponseDTO> createBooking(
            @Valid @RequestBody BookingRequestDTO bookingRequestDTO) {
        BookingResponseDTO response = bookingService.createBooking(bookingRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/get")
    public ResponseEntity<List<BookingResponseDTO>> getAllBookings() {
        List<BookingResponseDTO> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

}
