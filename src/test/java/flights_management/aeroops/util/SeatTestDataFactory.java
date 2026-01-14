package flights_management.aeroops.util;

import flights_management.aeroops.dto.seat.SeatRequestDTO;
import flights_management.aeroops.entity.Aircraft;
import flights_management.aeroops.entity.Flight;
import flights_management.aeroops.entity.Seat;
import flights_management.aeroops.enums.SeatClass;
import flights_management.aeroops.enums.Status;

public final class SeatTestDataFactory {

    private SeatTestDataFactory() {}

    // ~~~~~~~~~~~~~~~~ DTOS ~~~~~~~~~~~~~~~~

    public static SeatRequestDTO aSeatRequest() {
        return new SeatRequestDTO(1L, "12A", SeatClass.BUSINESS, true);
    }

    public static SeatRequestDTO aSeatRequestWithNewFlight() {
        return new SeatRequestDTO(2L, "10A", SeatClass.ECONOMY, false);
    }

    // ~~~~~~~~~~~~~~~~ AIRCRAFT ~~~~~~~~~~~~~~~~

    public static Aircraft anAircraft(int seats) {
        Aircraft aircraft = new Aircraft();
        aircraft.setSeats(seats);
        return aircraft;
    }

    // ~~~~~~~~~~~~~~~~ FLIGHTS ~~~~~~~~~~~~~~~~

    public static Flight aFlight() {
        Flight flight = new Flight();
        flight.setId(1L);
        flight.setFlightNumber("RO123");
        flight.setStatus(Status.PLANNED);
        flight.setAircraft(anAircraft(100));
        return flight;
    }

    public static Flight aCancelledFlight() {
        Flight flight = new Flight();
        flight.setId(1L);
        flight.setFlightNumber("RO123");
        flight.setStatus(Status.CANCELLED);
        flight.setAircraft(anAircraft(100));
        return flight;
    }

    public static Flight aDepartedFlight() {
        Flight flight = new Flight();
        flight.setId(2L);
        flight.setFlightNumber("RO123");
        flight.setStatus(Status.DEPARTED);
        flight.setAircraft(anAircraft(100));
        return flight;
    }

    public static Flight aNewFlight() {
        Flight flight = new Flight();
        flight.setId(2L);
        flight.setFlightNumber("RO342");
        flight.setStatus(Status.PLANNED);
        flight.setAircraft(anAircraft(150));
        return flight;
    }

    // ~~~~~~~~~~~~~~~~ SEATS ~~~~~~~~~~~~~~~~

    public static Seat aSeat() {
        Seat seat = new Seat();
        seat.setId(1L);
        seat.setFlight(aFlight());
        seat.setSeatNumber("12A");
        seat.setSeatClass(SeatClass.BUSINESS);
        seat.setIsAvailable(true);
        return seat;
    }

    public static Seat aSeatOnFlight(Flight flight,
                                     Long id,
                                     String seatNumber,
                                     SeatClass seatClass,
                                     boolean isAvailable) {
        Seat seat = new Seat();
        seat.setId(id);
        seat.setFlight(flight);
        seat.setSeatNumber(seatNumber);
        seat.setSeatClass(seatClass);
        seat.setIsAvailable(isAvailable);
        return seat;
    }
}
