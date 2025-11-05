package flights_management.aeroops.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Airport {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String iataCode; // ex: "LHR", "OTP"

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String timeZoneId; // ex: "Europe/Bucharest"
}
