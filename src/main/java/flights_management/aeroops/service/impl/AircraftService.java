package flights_management.aeroops.service.impl;

import flights_management.aeroops.dto.aircraft.AircraftRequestDTO;
import flights_management.aeroops.dto.aircraft.AircraftResponseDTO;
import flights_management.aeroops.entity.Aircraft;
import flights_management.aeroops.enums.ErrorCode;
import flights_management.aeroops.error.BusinessException;
import flights_management.aeroops.error.ErrorModel;
import flights_management.aeroops.mapper.AircraftMapper;
import flights_management.aeroops.repository.AircraftRepository;
import flights_management.aeroops.service.IAircraftService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AircraftService implements IAircraftService {

    private final AircraftRepository aircraftRepository;
    private final AircraftMapper aircraftMapper;

    @Override
    public AircraftResponseDTO createAircraft(AircraftRequestDTO requestDTO) {
        String normalizedRegistration = requestDTO.registration().toUpperCase();

        if (aircraftRepository.existsByRegistration(normalizedRegistration)) {
            ErrorModel error = new ErrorModel(ErrorCode.AIRCRAFT_REGISTRATION_EXISTS);
            throw new BusinessException(List.of(error));
        }

        // DTO -> Entity prin MapStruct
        Aircraft aircraft = aircraftMapper.toEntity(requestDTO);

        // persist
        Aircraft saved = aircraftRepository.save(aircraft);

        // Entity -> Response DTO
        return aircraftMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AircraftResponseDTO> getAllAircrafts() {
        return aircraftRepository.findAll()
                .stream()
                .map(aircraftMapper::toResponseDTO)
                .toList();
    }

    @Override
    public AircraftResponseDTO updateAircraft(Long id, AircraftRequestDTO requestDTO) {
        Aircraft aircraft = aircraftRepository.findById(id).orElseThrow(()-> new BusinessException(List.of(new ErrorModel(ErrorCode.AIRCRAFT_NOT_FOUND))));

        aircraft.setRegistration(requestDTO.registration());
        aircraft.setManufacturer(requestDTO.manufacturer());
        aircraft.setSeats(requestDTO.seats());
        aircraft.setType(requestDTO.type());

        Aircraft updated = aircraftRepository.save(aircraft);

        return aircraftMapper.toResponseDTO(updated);
    }

    @Override
    public void deleteAircraft(Long id) {
        Aircraft aircraft = aircraftRepository.findById(id).orElseThrow(()-> new BusinessException(List.of(new ErrorModel(ErrorCode.AIRCRAFT_NOT_FOUND))));
        aircraftRepository.delete(aircraft);
    }
}