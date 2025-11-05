package flights_management.aeroops.entity;

import flights_management.aeroops.enums.SeatClass;
import jakarta.persistence.*;
import lombok.Data;
//Seat(id, flight_id, seat_no, seat_class, is_available)
@Entity
@Data
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "flight_id", nullable = false)
    private Long flightId;
    @Column(name = "seat_no", nullable = false, length = 3)
    private String seatNumber;
    @Column(name = "seat_class")
    private SeatClass seatClass;
    @Column(name = "is_available")
    private Boolean isAvailable;
}
