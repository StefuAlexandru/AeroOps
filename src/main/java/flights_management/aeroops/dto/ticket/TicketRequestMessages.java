package flights_management.aeroops.dto.ticket;

public final class TicketRequestMessages {

    private TicketRequestMessages() {}

    public static final String BOOKING_ID_NOT_NULL = "Booking ID cannot be null";
    public static final String SEAT_ID_NOT_NULL = "Seat ID cannot be null";
    public static final String PRICE_NOT_NULL = "Price cannot be null";
    public static final String PRICE_MIN_MESSAGE = "Price must be > 0";
    public static final String PRICE_DIGITS_MESSAGE = "Price can have at most 10 digits before the comma, and 2 after it";
}
