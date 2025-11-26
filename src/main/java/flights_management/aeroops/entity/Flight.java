package flights_management.aeroops.entity;

import flights_management.aeroops.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;


//Flight(id, airline_id, flight_number, origin_airport_id, destination_airport_id, scheduled_departure, scheduled_arrival, status[PLANNED|ON_TIME|DELAYED|CANCELLED])


@Entity
@Getter
@Setter
@NoArgsConstructor
public class Flight{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Airline airline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Airport originAirport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Airport destinationAirport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Aircraft aircraft;

    @Column(nullable = false)
    private String flightNumber;
    @Column(nullable=false)
    private Instant scheduledDeparture;
    @Column(nullable=false)
    private Instant scheduledArrival;

    @Column(nullable=false)
    @Enumerated(EnumType.STRING)
    private Status status;


}
