package flights_management.aeroops.entity;

import flights_management.aeroops.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "flight_id")
    private Flight flight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "passenger_id")
    private Passenger passenger;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status = BookingStatus.HOLD; // default

    @Column(nullable = false, name = "price_total")
    private BigDecimal priceTotal;

    @Column(nullable = false, name = "created_at", updatable = false)
    private Instant createdAt = Instant.now();

}
