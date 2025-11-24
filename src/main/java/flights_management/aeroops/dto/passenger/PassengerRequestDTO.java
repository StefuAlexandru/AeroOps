package flights_management.aeroops.dto.passenger;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PassengerRequestDTO(
        @NotBlank(message = "First name cannot be blank")
        @Size(min = 3, max = 50)
        String firstName,

        @NotBlank(message = "Last name cannot be blank")
        @Size(min = 3, max = 50)
        String lastName,

        @NotBlank(message = "Phone number cannot be blank")
        @Email
        String email,

        @NotBlank(message = "Phone number cannot be blank")
        @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must contain 10–15 digits and may start with +")
        String phone
)
{ }
