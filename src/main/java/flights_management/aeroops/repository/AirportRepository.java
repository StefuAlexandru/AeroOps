package flights_management.aeroops.repository;

import flights_management.aeroops.entity.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirportRepository extends JpaRepository<Airport, Long> {
    boolean existsByIataCodeIgnoreCase(String iataCode);
}