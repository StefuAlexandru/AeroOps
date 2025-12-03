package flights_management.aeroops.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor
public class BusinessException extends RuntimeException {
    private final List<ErrorModel> errors;
}
