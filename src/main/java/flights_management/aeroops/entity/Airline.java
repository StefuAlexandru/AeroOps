package flights_management.aeroops.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class Airline {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) private String name;

    @Column(nullable = false, unique = true, length = 3)
    private String iataCode; // 2-3 litere (IATA/ICAO)

    @Column(nullable = false) private String country;

    @PrePersist @PreUpdate
    void normalize() {
        if (iataCode != null) iataCode = iataCode.trim().toUpperCase();
    }
}