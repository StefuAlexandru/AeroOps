package flights_management.aeroops.repository;

import flights_management.aeroops.entity.Booking;
import flights_management.aeroops.entity.Seat;
import flights_management.aeroops.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket,Long> {
    boolean existsBySeatId(Long seatId);

    @Query("""
            SELECT CASE WHEN count(t) > 0 THEN TRUE ELSE FALSE END
            FROM Ticket t
            WHERE t.booking = :booking
              AND t.seat = :seat
           """)
    boolean existsByBookingAndSeat(@Param("booking") Booking booking, @Param("seat") Seat seat);

    @Query("""
            SELECT CASE WHEN count(t) > 0 THEN TRUE ELSE FALSE END
            FROM Ticket t
            WHERE t.seat = :seat
            """)
    boolean existsBySeat(@Param("seat") Seat seat);

    @Query("""
            SELECT CASE WHEN count(t) > 0 THEN TRUE ELSE FALSE END
            FROM Ticket t
            WHERE t.seat = :seat
              AND t.id <> :ticketId
            """)
    boolean existsBySeatAndIdNot(@Param("seat") Seat seat, @Param("ticketId") Long ticketId);
}
