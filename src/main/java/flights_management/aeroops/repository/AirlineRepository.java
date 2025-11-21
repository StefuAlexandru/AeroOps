package flights_management.aeroops.repository;

import flights_management.aeroops.entity.Airline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirlineRepository extends JpaRepository<Airline, Long> {
    boolean existsByIataCodeIgnoreCase(String iataCode);
}