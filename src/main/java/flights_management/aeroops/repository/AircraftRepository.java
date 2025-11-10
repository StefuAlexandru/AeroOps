package flights_management.aeroops.repository;

import flights_management.aeroops.entity.Aircraft;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AircraftRepository extends JpaRepository<Aircraft, Long> {
    boolean existsByRegistration(String registration);
}
