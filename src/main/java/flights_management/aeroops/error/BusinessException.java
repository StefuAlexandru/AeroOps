package flights_management.aeroops.error;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class BusinessException extends RuntimeException {
    private final List<ErrorModel> errors;
    public BusinessException(List<ErrorModel> errors) {
        this.errors = errors;
    }

}
