package flights_management.aeroops.entity;

import flights_management.aeroops.enums.Status;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.ZonedDateTime;


//Flight(id, airline_id, flight_number, origin_airport_id, destination_airport_id, scheduled_departure, scheduled_arrival, status[PLANNED|ON_TIME|DELAYED|CANCELLED])


@Entity
@Getter
@Setter
@NoArgsConstructor
public class Flight{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Airline airline;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Airport originAirport;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Airport destinationAirport;

    private String flightNumber;
    private Instant scheduledDeparture;
    private Instant scheduledArrival;
    private Status status;


}
