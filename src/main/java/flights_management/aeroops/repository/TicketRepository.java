package flights_management.aeroops.repository;

import flights_management.aeroops.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket,Long> {
    boolean existsBySeatId(Long seatId);
}
