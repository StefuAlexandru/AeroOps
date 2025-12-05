package flights_management.aeroops.error;

import flights_management.aeroops.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ErrorModel {
    private String code;
    private String message;

    public ErrorModel(ErrorCode errorCode) {
        this.code = errorCode.name();
        this.message = errorCode.getMessage();
    }

    public ErrorModel(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
