package flights_management.aeroops.service.impl;

import flights_management.aeroops.dto.ticket.TicketRequestDTO;
import flights_management.aeroops.dto.ticket.TicketResponseDTO;
import flights_management.aeroops.entity.Booking;
import flights_management.aeroops.entity.Seat;
import flights_management.aeroops.entity.Ticket;
import flights_management.aeroops.enums.ErrorCode;
import flights_management.aeroops.enums.Status;
import flights_management.aeroops.error.BusinessException;
import flights_management.aeroops.error.ErrorModel;
import flights_management.aeroops.mapper.TicketMapper;
import flights_management.aeroops.repository.BookingRepository;
import flights_management.aeroops.repository.SeatRepository;
import flights_management.aeroops.repository.TicketRepository;
import flights_management.aeroops.service.ITicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TicketService implements ITicketService {
    private final TicketRepository ticketRepository;
    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;
    private final TicketMapper ticketMapper;

    @Override
    public TicketResponseDTO createTicket(TicketRequestDTO ticketRequestDTO){
        ValidatedTicketData validatedTicketData = validateFieldsAndFetchOrThrowIfError(ticketRequestDTO,null);

        Ticket ticket = ticketMapper.toEntity(
                ticketRequestDTO,
                validatedTicketData.booking(),
                validatedTicketData.seat());

        ticketRepository.save(ticket);

        makeSeatUnavailable(validatedTicketData);
        updateTotalPriceForBooking(validatedTicketData, ticket);

        return ticketMapper.toResponse(ticket);
    }

    private void makeSeatUnavailable(ValidatedTicketData validatedTicketData) {
        Seat seat = validatedTicketData.seat();
        seat.setIsAvailable(false);
        seatRepository.save(seat);
    }

    private void updateTotalPriceForBooking(ValidatedTicketData validatedTicketData, Ticket ticket) {
        Booking booking = validatedTicketData.booking();
        BigDecimal currentTotal = booking.getPriceTotal() != null ? booking.getPriceTotal() : BigDecimal.ZERO;

        booking.setPriceTotal(currentTotal.add(ticket.getPrice()));
        bookingRepository.save(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketResponseDTO> getAllTickets() {
        return ticketRepository.findAll()
                .stream().map(ticketMapper::toResponse).toList();
    }

    @Override
    public TicketResponseDTO updateTicket(Long id, TicketRequestDTO ticketRequestDTO) {
        Ticket ticketToUpdate =  ticketRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        List.of(new ErrorModel(ErrorCode.TICKET_NOT_FOUND))
                ));
        Booking oldBooking = ticketToUpdate.getBooking();
        BigDecimal oldPrice = ticketToUpdate.getPrice();

        Seat oldSeat = ticketToUpdate.getSeat();

        ValidatedTicketData validatedTicketData = validateFieldsAndFetchOrThrowIfError(ticketRequestDTO, ticketToUpdate.getId());
        Booking  newBooking = validatedTicketData.booking();
        Seat newSeat = validatedTicketData.seat();
        BigDecimal newPrice = ticketRequestDTO.price();

        ticketToUpdate.setSeat(newSeat);
        ticketToUpdate.setBooking(newBooking);
        ticketToUpdate.setPrice(newPrice);
        ticketToUpdate.setUpdatedAt(Instant.now());

        updateOldAndNewSeat(oldSeat, newSeat);
        updateBookingPrice(oldBooking, newBooking, oldPrice, newPrice);

        ticketRepository.save(ticketToUpdate);
        return ticketMapper.toResponse(ticketToUpdate);

    }

    private void updateBookingPrice(Booking oldBooking, Booking newBooking, BigDecimal oldPrice, BigDecimal newPrice) {
        if (oldBooking.getId().equals(newBooking.getId())) {
            // Same booking -> adjust only priceTotal
            BigDecimal total = defaultZero(newBooking.getPriceTotal());
            total = total.subtract(oldPrice).add(newPrice);
            newBooking.setPriceTotal(total);
            bookingRepository.save(newBooking);
        } else {
            // Change ticket on another booking
            updateBookingTotalPriceWhenDeleteTicket(oldBooking, oldPrice);

            BigDecimal newTotal = defaultZero(newBooking.getPriceTotal());
            newBooking.setPriceTotal(newTotal.add(newPrice));
            bookingRepository.save(newBooking);
        }
    }

    private BigDecimal defaultZero(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }

    private void updateOldAndNewSeat(Seat oldSeat, Seat newSeat) {
        // If seat has changed, free old one and book the new one
        if (!oldSeat.getId().equals(newSeat.getId())) {
            // Free old Seat only if no more tickets exists for him
            if (!ticketRepository.existsBySeat(oldSeat)) {
                oldSeat.setIsAvailable(true);
                seatRepository.save(oldSeat);
            }

            newSeat.setIsAvailable(false);
            seatRepository.save(newSeat);
        }
    }

    @Override
    public void deleteTicket(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        List.of(new ErrorModel(ErrorCode.TICKET_NOT_FOUND))
                ));

        Seat seat = ticket.getSeat();
        Booking booking = ticket.getBooking();

        ticketRepository.delete(ticket);

        makeSeatAvailableIfNoTickets(seat);
        updateBookingTotalPriceWhenDeleteTicket(booking, ticket.getPrice());
    }

    private void updateBookingTotalPriceWhenDeleteTicket(Booking booking, BigDecimal ticketPrice) {
        BigDecimal total = defaultZero(booking.getPriceTotal());
        booking.setPriceTotal(total.subtract(ticketPrice));
        bookingRepository.save(booking);
    }

    private void makeSeatAvailableIfNoTickets(Seat seat) {
        // If after delete process Seat isn't use by another Ticket it becomes available
        if (!ticketRepository.existsBySeat(seat)) {
            seat.setIsAvailable(true);
            seatRepository.save(seat);
        }
    }

    private record ValidatedTicketData(Booking booking,Seat seat){ }

    private ValidatedTicketData validateFieldsAndFetchOrThrowIfError(TicketRequestDTO ticketRequestDTO, Long currentTicketId) {
        List<ErrorModel> errors = new ArrayList<>();

        Booking booking = bookingRepository.findById(ticketRequestDTO.bookingId()).orElse(null);
        if (booking == null) {
            errors.add(new ErrorModel(ErrorCode.BOOKING_NOT_FOUND));
        }

        Seat seat = seatRepository.findById(ticketRequestDTO.seatId()).orElse(null);
        if (seat == null) {
            errors.add(new ErrorModel(ErrorCode.SEAT_NOT_FOUND));
        }

        validateTicketPrice(ticketRequestDTO, errors);

        if (booking == null || seat == null) {
            throwIfErrors(errors);
        }

        ValidatedTicketData validatedTicketData = new ValidatedTicketData(booking, seat);

        validateCreateConstraints(validatedTicketData.seat(), validatedTicketData.booking(), errors);
        validateSeat(validatedTicketData.seat(), currentTicketId, errors);

        throwIfErrors(errors);
        return validatedTicketData;
    }


    private void validateCreateConstraints(Seat seat,Booking booking,List<ErrorModel> errors){
        boolean notSameFlight = !seat.getFlight().getId().equals(booking.getFlight().getId());
        // You cannot create/update Ticket if the Seat is not on the same flight with Booking flight
        if (notSameFlight) {
            errors.add(new ErrorModel(ErrorCode.SEAT_NOT_IN_BOOKING_FLIGHT));
        }

        // You cannot create/update Ticket if the Booking status is canceled
        if (booking.getStatus().isTerminal()) {
            errors.add(new ErrorModel(ErrorCode.BOOKING_NOT_ACTIVE));
        }

        // You cannot create/update Ticket if associated flight is departed or canceled
        Status flightStatus = booking.getFlight().getStatus();
        if (flightStatus.isCancelled()) {
            errors.add(new ErrorModel(ErrorCode.FLIGHT_CANCELLED));
        }
        if (flightStatus.isDeparted()) {
            errors.add(new ErrorModel(ErrorCode.FLIGHT_DEPARTED));
        }
    }

    private void validateSeat(Seat seat, Long currentTicketId, List<ErrorModel> errors) {
        // Check if Seat is used on another ticket
        // If currentTicketId == null -> CREATE -> Check if any Ticket exists on this Seat
        // If currentTicketId != null > UPDATE -> Check if Ticket exists for seat,
        // but with another id than current ticket
        boolean seatUsedByOther = (currentTicketId == null)
                ? ticketRepository.existsBySeat(seat)  // create
                : ticketRepository.existsBySeatAndIdNot(seat, currentTicketId); // update

        if (seatUsedByOther) {
            errors.add(new ErrorModel(ErrorCode.SEAT_ALREADY_ALLOCATED));
        }

        // Check Seat availability when creating
        if (currentTicketId == null && Boolean.FALSE.equals(seat.getIsAvailable())) {
            errors.add(new ErrorModel(ErrorCode.SEAT_NOT_AVAILABLE));
        }
    }

    private void validateTicketPrice(TicketRequestDTO ticketRequestDTO,List<ErrorModel> errors){
        BigDecimal price = ticketRequestDTO.price();
        if(price == null || price.compareTo(BigDecimal.ZERO) <= 0){
            errors.add(new ErrorModel(ErrorCode.INVALID_TICKET_PRICE));
        }
    }

    private void throwIfErrors(List<ErrorModel> errors) {
        if (!errors.isEmpty()) {
            throw new BusinessException(errors);
        }
    }

}

