package flights_management.aeroops.error;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import flights_management.aeroops.enums.ErrorCode;
import flights_management.aeroops.enums.SeatClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ControllerAdvice
public class CustomExceptionHandler {


    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<List<ErrorModel>> handleBusinessException(BusinessException ex){
        HttpStatus status = ex.getErrors().stream()
                .map(error -> ErrorCode.valueOf(error.getCode()).getStatus())
                .findFirst()
                .orElse(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(ex.getErrors(),status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorModel>> handleFieldValidationException(MethodArgumentNotValidException ex){
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        List<ErrorModel> errors = new ArrayList<>();
        fieldErrors.forEach(fieldError -> {
           ErrorModel errorModel = new ErrorModel(fieldError.getCode(), fieldError.getDefaultMessage());
           errors.add(errorModel);
        });
        return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<List<ErrorModel>> handleJsonParseError(HttpMessageNotReadableException ex) {
        List<ErrorModel> errors = new ArrayList<>();

        if (ex.getCause() instanceof InvalidFormatException invalidFormatException
                && invalidFormatException.getTargetType().equals(SeatClass.class)) {

            errors.add(new ErrorModel(
                    "InvalidSeatClass",
                    "Seat class must be one of: " + Arrays.toString(SeatClass.values())
            ));

            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        errors.add(new ErrorModel("InvalidRequestBody", "Malformed JSON"));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}

