package flights_management.aeroops.dto.seat;

public final class SeatRequestMessages {

    private SeatRequestMessages() {}

    public static final String FLIGHT_ID_NOT_NULL = "Flight ID cannot be null";
    public static final String SEAT_NUMBER_NOT_NULL = "Seat number cannot be null";
    public static final String SEAT_NUMBER_INVALID = "Seat number must be in format like '12A' (1-2 digits followed by a letter A–G)";
    public static final String SEAT_CLASS_NOT_NULL = "Seat class cannot be null";
    public static final String SEAT_AVAILABILITY_NOT_NULL = "Seat availability cannot be null";
}
