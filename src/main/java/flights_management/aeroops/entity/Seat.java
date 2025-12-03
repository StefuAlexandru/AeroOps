package flights_management.aeroops.entity;

import flights_management.aeroops.enums.SeatClass;
import flights_management.aeroops.util.TimeStamps;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//Seat(id, flight_id, seat_no, seat_class, is_available)
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Seat extends TimeStamps {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Flight flight;

    @Column(nullable = false, length = 3)
    private String seatNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SeatClass seatClass;

    @Column(nullable = false)
    private Boolean isAvailable;
}
