package flights_management.aeroops.enums;

public enum SeatClass {
    ECONOMY("Economy Class"),
    BUSINESS("Business Class"),
    FIRST("First Class");
    private final String label;

    SeatClass(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
