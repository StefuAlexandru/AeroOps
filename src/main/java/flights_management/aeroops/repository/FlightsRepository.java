package flights_management.aeroops.repository;

import flights_management.aeroops.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightsRepository extends JpaRepository<Flight,Long> { }
