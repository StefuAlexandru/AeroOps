package flights_management.aeroops.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import flights_management.aeroops.dto.seat.SeatRequestDTO;
import flights_management.aeroops.dto.seat.SeatResponseDTO;
import flights_management.aeroops.enums.ErrorCode;
import flights_management.aeroops.enums.SeatClass;
import flights_management.aeroops.error.BusinessException;
import flights_management.aeroops.error.CustomExceptionHandler;
import flights_management.aeroops.error.ErrorModel;
import flights_management.aeroops.service.impl.SeatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static flights_management.aeroops.dto.seat.SeatRequestMessages.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class SeatControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SeatService seatService;

    @InjectMocks
    private SeatController seatController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(seatController)
                .setControllerAdvice(new CustomExceptionHandler())
                .build();

    }

    private String createRequestBody(Long flightId, String seatNumber, String seatClass, Boolean isAvailable) throws JsonProcessingException {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("flightId", flightId);
        body.put("seatNumber", seatNumber);
        body.put("seatClass", seatClass);
        body.put("isAvailable", isAvailable);

        return objectMapper.writeValueAsString(body);
    }

    // ~~~~~~~~~~~~~~~~ CREATE ~~~~~~~~~~~~~~~~

    @Tag("create")
    @Tag("happy-path")
    @Test
    void createSeat_shouldReturn201AndResponseBody() throws Exception{
        // Arrange
        SeatResponseDTO seatResponseDTO = new SeatResponseDTO(10L,"12A", SeatClass.BUSINESS,true,"RO123",null,null);

        String requestBody = createRequestBody(10L,"12A","BUSINESS",true);

        when(seatService.createSeat(any())).thenReturn(seatResponseDTO);

        // Act + Assert
        mockMvc.perform(post("/api/seat")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.id").value(10),
                        jsonPath("$.seatNumber").value("12A"),
                        jsonPath("$.seatClass").value("BUSINESS"),
                        jsonPath("$.isAvailable").value(true),
                        jsonPath("$.flightNumber").value("RO123")
                );
    }

    @Tag("create")
    @Tag("validation")
    @Test
    void createSeat_shouldReturn400_whenFlightIdIsNull() throws Exception{
        // Arrange
        String requestBody = createRequestBody(null,"12A","BUSINESS",true);

        //Act
        mockMvc.perform(post("/api/seat")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$[0].code").value("NotNull"),
                        jsonPath("$[0].message").value(FLIGHT_ID_NOT_NULL)
                );
    }

    @Tag("create")
    @Tag("validation")
    @Test
    void createSeat_shouldReturn400_whenSeatNumberNull() throws Exception{
        // Arrange
        String requestBody = createRequestBody(10L,null,"BUSINESS",true);

        //Act
        mockMvc.perform(post("/api/seat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$[0].code").value("NotNull"),
                        jsonPath("$[0].message").value(SEAT_NUMBER_NOT_NULL)
                );
    }

    @Tag("create")
    @Tag("validation")
    @Test
    void createSeat_shouldReturn400_whenSeatNumberWrong() throws Exception{
        // Arrange
        String requestBody = createRequestBody(10L,"01A","BUSINESS",true);

        //Act
        mockMvc.perform(post("/api/seat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$[0].code").value("Pattern"),
                        jsonPath("$[0].message").value(SEAT_NUMBER_INVALID)
                );
    }

    @Tag("create")
    @Tag("validation")
    @Test
    void createSeat_shouldReturn400_whenSeatClassIsNull() throws Exception{
        // Arrange
        String requestBody = createRequestBody(10L,"12A",null,true);

        //Act
        mockMvc.perform(post("/api/seat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$[0].code").value("NotNull"),
                        jsonPath("$[0].message").value(SEAT_CLASS_NOT_NULL)
                );
    }

    @Tag("create")
    @Tag("validation")
    @Test
    void createSeat_shouldReturn400_whenSeatClassWrong() throws Exception{
        // Arrange
        String requestBody = createRequestBody(10L,"12A","SALAM",true);

        //Act
        mockMvc.perform(post("/api/seat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$[0].code").value("InvalidSeatClass"),
                        jsonPath("$[0].message").exists()
                );
    }

    @Tag("create")
    @Tag("validation")
    @Test
    void createSeat_shouldReturn400_whenSeatAvailabilityIsNull() throws Exception{
        // Arrange
        String requestBody = createRequestBody(10L,"12A","BUSINESS",null);

        //Act
        mockMvc.perform(post("/api/seat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$[0].code").value("NotNull"),
                        jsonPath("$[0].message").value(SEAT_AVAILABILITY_NOT_NULL)
                );
    }

    @Tag("get")
    @Test
    void getSeat_shouldReturn200() throws Exception{
        // Arrange
        SeatResponseDTO seatResponseDTO = new SeatResponseDTO(10L,"12A", SeatClass.BUSINESS,true,"RO123",null,null);

        when(seatService.getAllSeats()).thenReturn(List.of(seatResponseDTO));

        // Act
        mockMvc.perform(get("/api/seat"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.length()").value(1),
                        jsonPath("$[0].id").value(10L),
                        jsonPath("$[0].seatNumber").value("12A"),
                        jsonPath("$[0].seatClass").value("BUSINESS"),
                        jsonPath("$[0].isAvailable").value(true),
                        jsonPath("$[0].flightNumber").value("RO123")
                );

        verify(seatService).getAllSeats();
        verifyNoMoreInteractions(seatService);
    }

    @Tag("get")
    @Test
    void getSeat_shouldReturn200_whenNoRecordsHasFound() throws Exception{
        // Arrange
        when(seatService.getAllSeats()).thenReturn(List.of());

        // Act
        mockMvc.perform(get("/api/seat"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.length()").value(0)
                );

        verify(seatService).getAllSeats();
        verifyNoMoreInteractions(seatService);
    }

    @Tag("update")
    @Tag("happy-path")
    @Test
    void updateSeat_shouldReturn200AndResponseBody() throws Exception{
        // Arrange
        String requestBody = createRequestBody(2L, "10A", "ECONOMY", false);
        SeatResponseDTO updatedSeatResponse = new SeatResponseDTO(10L, "10A", SeatClass.ECONOMY, false, "RO342", null, null);

        when(seatService.updateSeat(eq(1L), any(SeatRequestDTO.class))).thenReturn(updatedSeatResponse);

        mockMvc.perform(put("/api/seat/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.id").value(10),
                        jsonPath("$.seatNumber").value("10A"),
                        jsonPath("$.seatClass").value("ECONOMY"),
                        jsonPath("$.isAvailable").value(false),
                        jsonPath("$.flightNumber").value("RO342")
                );

        verify(seatService).updateSeat(eq(1L), any(SeatRequestDTO.class));
        verifyNoMoreInteractions(seatService);
    }

    @Tag("update")
    @Tag("validation")
    @Test
    void updateSeat_shouldReturn400_whenFlightIdIsNull() throws Exception {
        // Arrange
        String requestBody = createRequestBody(null, "10A", "ECONOMY", true);

        // Act + Assert
        mockMvc.perform(put("/api/seat/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$[0].code").value("NotNull"),
                        jsonPath("$[0].message").value(FLIGHT_ID_NOT_NULL)
                );

        verifyNoInteractions(seatService);
    }

    @Tag("update")
    @Tag("validation")
    @Test
    void updateSeat_shouldReturn400_whenSeatNumberIsNull() throws Exception {
        // Arrange
        String requestBody = createRequestBody(10L, null, "ECONOMY", true);

        // Act + Assert
        mockMvc.perform(put("/api/seat/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$[0].code").value("NotNull"),
                        jsonPath("$[0].message").value(SEAT_NUMBER_NOT_NULL)
                );

        verifyNoInteractions(seatService);
    }

    @Tag("update")
    @Tag("validation")
    @Test
    void updateSeat_shouldReturn400_whenSeatClassIsInvalid() throws Exception {
        // Arrange
        String requestBody = createRequestBody(10L, null, "SALAM", true);

        // Act + Assert
        mockMvc.perform(put("/api/seat/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$[0].code").value("InvalidSeatClass"),
                        jsonPath("$[0].message").exists()
                );

        verifyNoInteractions(seatService);
    }


    @Tag("update")
    @Tag("validation")
    @Test
    void updateSeat_shouldReturn400_whenSeatNumberInvalidFormat() throws Exception {
        // Arrange
        String requestBody = createRequestBody(10L, "XYZ", "ECONOMY", true);

        // Act + Assert
        mockMvc.perform(put("/api/seat/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$[0].code").value("Pattern"),
                        jsonPath("$[0].message").value(SEAT_NUMBER_INVALID)
                );

        verifyNoInteractions(seatService);
    }

    @Tag("update")
    @Tag("validation")
    @Test
    void updateSeat_shouldReturn400_whenSeatClassIsNull() throws Exception {
        // Arrange
        String requestBody = createRequestBody(10L, "12A", null, true);

        // Act + Assert
        mockMvc.perform(put("/api/seat/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$[0].code").value("NotNull"),
                        jsonPath("$[0].message").value(SEAT_CLASS_NOT_NULL)
                );

        verifyNoInteractions(seatService);
    }

    @Tag("update")
    @Tag("validation")
    @Test
    void updateSeat_shouldReturn400_whenIsAvailableIsNull() throws Exception {
        // Arrange
        String requestBody = createRequestBody(10L, "12A", "ECONOMY", null);

        // Act + Assert
        mockMvc.perform(put("/api/seat/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$[0].code").value("NotNull"),
                        jsonPath("$[0].message").value(SEAT_AVAILABILITY_NOT_NULL)
                );

        verifyNoInteractions(seatService);
    }


    @Tag("delete")
    @Tag("happy-path")
    @Test
    void deleteSeat_shouldReturn204() throws Exception {
        // Arrange
        doNothing().when(seatService).deleteSeat(1L);

        // Act + Assert
        mockMvc.perform(delete("/api/seat/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(seatService).deleteSeat(1L);
        verifyNoMoreInteractions(seatService);
    }

    @Tag("delete")
    @Tag("validation")
    @Test
    void deleteSeat_shouldReturn404_whenSeatNotFound() throws Exception {
        // Arrange
        doThrow(new BusinessException(List.of(new ErrorModel(ErrorCode.SEAT_NOT_FOUND))))
                .when(seatService).deleteSeat(999L);

        // Act + Assert
        mockMvc.perform(delete("/api/seat/{id}", 999L))
                .andExpect(status().isNotFound());

        verify(seatService).deleteSeat(999L);
        verifyNoMoreInteractions(seatService);
    }

}