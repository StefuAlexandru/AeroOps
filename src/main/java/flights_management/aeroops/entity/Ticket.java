package flights_management.aeroops.entity;

import flights_management.aeroops.util.TimeStamps;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

//Ticket(id, booking_id, seat_id, price)
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Ticket extends TimeStamps {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Booking booking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Seat seat;

    @Column(nullable = false)
    private BigDecimal price;
}
