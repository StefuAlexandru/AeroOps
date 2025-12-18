package flights_management.aeroops.enums;

public enum BookingStatus {
    HOLD, CONFIRMED, CANCELLED;

    public boolean isActive() {
        return this == HOLD || this == CONFIRMED;
    }

    public boolean isTerminal() {
        return this == CANCELLED;
    }

    public boolean canIssueTicket() {
        return this == CONFIRMED;
    }

    public boolean canTransitionTo(BookingStatus next) {
        return switch (this) {
            case HOLD      -> next == CONFIRMED || next == CANCELLED;
            case CONFIRMED -> next == CANCELLED;
            case CANCELLED -> false;
        };
    }
}
