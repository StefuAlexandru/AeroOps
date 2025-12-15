package flights_management.aeroops.repository;

import flights_management.aeroops.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepository extends JpaRepository<Seat,Long> {
    boolean existsByFlightIdAndSeatNumber(Long flightId, String seatNumber);
    long countByFlightId(Long flightId);
    boolean existsByFlightIdAndSeatNumberAndIdNot(Long flightId, String seatNumber, Long id);
}
