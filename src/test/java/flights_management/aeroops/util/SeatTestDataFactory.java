package flights_management.aeroops.util;

import flights_management.aeroops.dto.seat.SeatRequestDTO;
import flights_management.aeroops.entity.Aircraft;
import flights_management.aeroops.entity.Flight;
import flights_management.aeroops.entity.Seat;
import flights_management.aeroops.enums.SeatClass;
import flights_management.aeroops.enums.Status;
import lombok.NoArgsConstructor;


@NoArgsConstructor
public final class SeatTestDataFactory {

    public static SeatRequestDTO request = new SeatRequestDTO(1L, "12A",SeatClass.BUSINESS, true);
    public static SeatRequestDTO requestWithNewFlight = new SeatRequestDTO(2L, "10A", SeatClass.ECONOMY, false);

    private static final Aircraft aircraft = createAircraft();

    public static Aircraft createAircraft() {
        Aircraft aircraft = new Aircraft();
        aircraft.setSeats(100);
        return aircraft;
    }

    public static Flight createNewFlight(){
        Aircraft newAircraft = new Aircraft();
        newAircraft.setSeats(150);

        Flight newFlight = new Flight();
        newFlight.setId(2L);
        newFlight.setFlightNumber("RO342");
        newFlight.setStatus(Status.PLANNED);
        newFlight.setAircraft(newAircraft);

        return  newFlight;
    }

    public static Flight createFlight() {
        Flight flight = new Flight();
        flight.setId(1L);
        flight.setFlightNumber("RO123");
        flight.setStatus(Status.PLANNED);
        flight.setAircraft(aircraft);
        return flight;
    }

    public static Flight createCancelledFlight() {
        Flight flight = new Flight();
        flight.setId(1L);
        flight.setFlightNumber("RO123");
        flight.setStatus(Status.CANCELLED);
        flight.setAircraft(aircraft);
        return flight;
    }

    public static Flight createDepartedFlight() {
        Flight flight = new Flight();
        flight.setId(2L);
        flight.setFlightNumber("RO123");
        flight.setStatus(Status.DEPARTED);
        flight.setAircraft(aircraft);
        return flight;
    }


    public static Seat createSeat() {
        Seat seat = new Seat();
        seat.setId(1L);
        seat.setFlight(createFlight());
        seat.setSeatNumber("12A");
        seat.setSeatClass(SeatClass.BUSINESS);
        seat.setIsAvailable(true);
        return seat;
    }
}
