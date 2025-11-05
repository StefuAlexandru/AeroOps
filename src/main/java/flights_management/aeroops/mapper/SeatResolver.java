package flights_management.aeroops.mapper;

import flights_management.aeroops.entity.Seat;
import flights_management.aeroops.repository.SeatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SeatResolver {
    private final SeatsRepository seatsRepository;

    public Seat fromId(Long seatId) {
        if (seatId == null) return null;
        return seatsRepository.findById(seatId)
                .orElseThrow(() -> new IllegalArgumentException("Seat not found with id " + seatId));
    }
}
