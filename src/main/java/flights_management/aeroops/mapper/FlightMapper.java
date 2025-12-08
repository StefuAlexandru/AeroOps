package flights_management.aeroops.mapper;


import flights_management.aeroops.dto.aircraft.AircraftDTO;
import flights_management.aeroops.dto.airline.AirlineDTO;
import flights_management.aeroops.dto.airport.AirportDTO;
import flights_management.aeroops.dto.flight.FlightDTO;
import flights_management.aeroops.dto.flight.FlightRequestDTO;
import flights_management.aeroops.dto.flight.FlightResponseDTO;
import flights_management.aeroops.entity.Aircraft;
import flights_management.aeroops.entity.Airline;
import flights_management.aeroops.entity.Airport;
import flights_management.aeroops.entity.Flight;
import org.mapstruct.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Mapper(
        componentModel = "spring",
        uses = {AirlineMapper.class, AirportMapper.class, AircraftMapper.class}
)
public interface FlightMapper {

    // RequestDTO -> Entity

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "airline", source = "airline")
    @Mapping(target = "originAirport", source = "originAirport")
    @Mapping(target = "destinationAirport", source = "destinationAirport")
    @Mapping(target = "aircraft",source = "aircraft")
    @Mapping(target = "status", constant = "PLANNED")
    @Mapping(target = "scheduledDeparture", expression = "java(toInstant(dto.scheduledDeparture()))")
    @Mapping(target = "scheduledArrival",  expression = "java(toInstant(dto.scheduledArrival()))")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Flight toEntity(FlightRequestDTO dto, Airline airline, Airport originAirport, Airport destinationAirport, Aircraft aircraft);

    // Entity -> ResponseDTO

    @Mapping(target = "airline", source = "airline")
    @Mapping(target = "originAirport", source = "originAirport")
    @Mapping(target = "destinationAirport", source = "destinationAirport")
    @Mapping(target = "aircraft" , source = "aircraft")
    @Mapping(target = "scheduledDeparture",
            expression = "java(toZoned(flight.getScheduledDeparture(),flight.getOriginAirport()))")
    @Mapping(target = "scheduledArrival",
            expression = "java(toZoned(flight.getScheduledArrival(),flight.getDestinationAirport()))")
    FlightResponseDTO toResponse(Flight flight);


    //Entity -> DTO


    @Mapping(target = "airlineName" , source = "airline.name")
    @Mapping(target = "originIata" , source = "originAirport.iataCode")
    @Mapping(target = "destinationIata" , source = "destinationAirport.iataCode")
    @Mapping(target = "aircraftRegistration" , source = "aircraft.registration")
    @Mapping(target = "scheduledDeparture",
            expression = "java(toZoned(flight.getScheduledDeparture(),flight.getOriginAirport()))")
    @Mapping(target = "scheduledArrival",
            expression = "java(toZoned(flight.getScheduledArrival(),flight.getDestinationAirport()))")
    FlightDTO toDto(Flight flight);


    default Instant toInstant(ZonedDateTime zonedDateTime){
        return zonedDateTime == null ? null : zonedDateTime.toInstant();
    }

    default ZonedDateTime toZoned(Instant instant, Airport airport){
        if(instant == null) return null;
        String timeZone = airport != null ? airport.getTimeZoneId() : null;
        ZoneId zone = (timeZone != null && !timeZone.isBlank()) ? ZoneId.of(timeZone) : ZoneId.of("UTC");
        return instant.atZone(zone);
    }

}
