package flights_management.aeroops.entity;

import jakarta.persistence.*;
import lombok.Data;

//Ticket(id, booking_id, seat_id, price)
@Entity
@Data
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "booking_id")
    private Long bookingId;

    @ManyToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @Column(name = "price")
    private Double price;
}
