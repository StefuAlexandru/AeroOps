package flights_management.aeroops.error;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorModel {
    public String code;
    public String message;
}
