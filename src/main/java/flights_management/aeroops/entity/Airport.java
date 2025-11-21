package flights_management.aeroops.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class Airport {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 3)
    private String iataCode; // 3 litere

    @Column(nullable = false) private String name;
    @Column(nullable = false) private String city;
    @Column(nullable = false) private String country;
    @Column(nullable = false) private String timeZoneId;

    @PrePersist @PreUpdate
    void normalize() {
        if (iataCode != null) iataCode = iataCode.trim().toUpperCase();
        if (timeZoneId != null) timeZoneId = timeZoneId.trim();
    }
}