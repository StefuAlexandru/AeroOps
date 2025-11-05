package flights_management.aeroops.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Airline {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    // IATA pentru companii (ex: "LH", "RO") sau ICAO (3 litere) — îl păstrăm unic
    @Column(nullable = false, unique = true)
    private String iataCode;

    @Column(nullable = false)
    private String country;
}
