package flights_management.aeroops.dto.airport;

import jakarta.validation.constraints.*;

public record AircraftRequest(
        @NotBlank @Pattern(regexp = "^[A-Z0-9-]{3,16}$") String registration,
        @NotBlank String type,
        @NotBlank String manufacturer,
        @NotNull @Min(1) @Max(1000) Integer seats
) {}
