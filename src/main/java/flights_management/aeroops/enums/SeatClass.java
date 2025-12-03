package flights_management.aeroops.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SeatClass {
    ECONOMY("Economy Class"),
    BUSINESS("Business Class"),
    FIRST("First Class");
    private final String label;
}
