package flights_management.aeroops.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import flights_management.aeroops.dto.ticket.TicketRequestDTO;
import flights_management.aeroops.dto.ticket.TicketResponseDTO;
import flights_management.aeroops.enums.BookingStatus;
import flights_management.aeroops.enums.ErrorCode;
import flights_management.aeroops.enums.SeatClass;
import flights_management.aeroops.error.BusinessException;
import flights_management.aeroops.error.CustomExceptionHandler;
import flights_management.aeroops.error.ErrorModel;
import flights_management.aeroops.service.impl.TicketService;
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

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static flights_management.aeroops.dto.ticket.TicketRequestMessages.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
class TicketControllerTest{

    private MockMvc mockMvc;

    @Mock
    private TicketService ticketService;

    @InjectMocks
    private TicketController ticketController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(ticketController)
                .setControllerAdvice(new CustomExceptionHandler())
                .build();
    }

    private String createRequestBody(Long bookingId, Long seatId, BigDecimal price) throws JsonProcessingException {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("bookingId", bookingId);
        body.put("seatId", seatId);
        body.put("price", price);

        return objectMapper.writeValueAsString(body);
    }

    // ~~~~~~~~~~~~~~~~ CREATE ~~~~~~~~~~~~~~~~

    @Tag("create")
    @Tag("happy-path")
    @Test
    void createTicket_shouldReturn201AndResponseBody() throws Exception {
        // Arrange
        TicketResponseDTO ticketResponseDTO = new TicketResponseDTO(1L,"123", BookingStatus.HOLD,BigDecimal.valueOf(100),"12A", SeatClass.BUSINESS,null,null);

        String requestBody = createRequestBody(1L,1L,BigDecimal.valueOf(100));

        when(ticketService.createTicket(any())).thenReturn(ticketResponseDTO);

        // Act + Assert
        mockMvc.perform(post("/api/ticket")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.id").value(1),
                        jsonPath("$.code").value("123"),
                        jsonPath("$.bookingStatus").value("HOLD"),
                        jsonPath("$.price").value(BigDecimal.valueOf(100)),
                        jsonPath("$.seatNumber").value("12A"),
                        jsonPath("$.seatClass").value("BUSINESS")
                );
        verify(ticketService).createTicket(any(TicketRequestDTO.class));
        verifyNoMoreInteractions(ticketService);
    }

    @Tag("create")
    @Tag("validation")
    @Test
    void createTicket_shouldReturn400_whenBookingIdIsNull() throws Exception {
        // Arrange
        String requestBody = createRequestBody(null, 1L, BigDecimal.valueOf(100));

        // Act + Assert
        mockMvc.perform(post("/api/ticket")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                        .andExpectAll(
                                status().isBadRequest(),
                                jsonPath("$[0].code").value("NotNull"),
                                jsonPath("$[0].message").value(BOOKING_ID_NOT_NULL)
                        );

        verifyNoInteractions(ticketService);
    }

    @Tag("create")
    @Tag("validation")
    @Test
    void createTicket_shouldReturn400_whenSeatIdIsNull() throws Exception {
        // Arrange
        String requestBody = createRequestBody(1L, null, BigDecimal.valueOf(100));

        // Act + Assert
        mockMvc.perform(post("/api/ticket")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                        .andExpectAll(
                                status().isBadRequest(),
                                jsonPath("$[0].code").value("NotNull"),
                                jsonPath("$[0].message").value(SEAT_ID_NOT_NULL)
                        );

        verifyNoInteractions(ticketService);
    }

    @Tag("create")
    @Tag("validation")
    @Test
    void createTicket_shouldReturn400_whenPriceIsNull() throws Exception {
        // Arrange
        String requestBody = createRequestBody(1L, 1L, null);

        // Act + Assert
        mockMvc.perform(post("/api/ticket")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                        .andExpectAll(
                                status().isBadRequest(),
                                jsonPath("$[0].code").value("NotNull"),
                                jsonPath("$[0].message").value(PRICE_NOT_NULL)
                        );

        verifyNoInteractions(ticketService);
    }

    @Tag("create")
    @Tag("validation")
    @Test
    void createTicket_shouldReturn400_whenPriceIsZero() throws Exception {
        // Arrange
        String requestBody = createRequestBody(1L, 1L, new BigDecimal("0.00"));

        // Act + Assert
        mockMvc.perform(post("/api/ticket")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                        .andExpectAll(
                                status().isBadRequest(),
                                jsonPath("$[0].code").value("DecimalMin"),
                                jsonPath("$[0].message").value(PRICE_MIN_MESSAGE)
                        );

        verifyNoInteractions(ticketService);
    }

    @Tag("create")
    @Tag("validation")
    @Test
    void createTicket_shouldReturn400_whenPriceIsNegative() throws Exception {
        // Arrange
        String requestBody = createRequestBody(1L, 1L, new BigDecimal("-10.00"));

        // Act + Assert
        mockMvc.perform(post("/api/ticket")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                        .andExpectAll(
                                status().isBadRequest(),
                                jsonPath("$[0].code").value("DecimalMin"),
                                jsonPath("$[0].message").value(PRICE_MIN_MESSAGE)
                        );

        verifyNoInteractions(ticketService);
    }

    @Tag("create")
    @Tag("validation")
    @Test
    void createTicket_shouldReturn400_whenPriceHasTooManyFractionDigits() throws Exception {
        String requestBody = createRequestBody(1L, 1L, new BigDecimal("100.123"));

        // Act + Assert
        mockMvc.perform(post("/api/ticket")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                        .andExpectAll(
                                status().isBadRequest(),
                                jsonPath("$[0].code").value("Digits"),
                                jsonPath("$[0].message").value(PRICE_DIGITS_MESSAGE)
                        );

        verifyNoInteractions(ticketService);
    }

    @Tag("create")
    @Tag("validation")
    @Test
    void createTicket_shouldReturn400_whenPriceHasTooManyIntegerDigits() throws Exception {
        String requestBody = createRequestBody(1L, 1L, new BigDecimal("12345678901.00"));

        // Act + Assert
        mockMvc.perform(post("/api/ticket")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                        .andExpectAll(
                                status().isBadRequest(),
                                jsonPath("$[0].code").value("Digits"),
                                jsonPath("$[0].message").value(PRICE_DIGITS_MESSAGE)
                        );

        verifyNoInteractions(ticketService);
    }

    // ~~~~~~~~~~~~~~~~ GET ~~~~~~~~~~~~~~~~

    @Tag("get")
    @Test
    void getAllTickets_shouldReturn200_withResults() throws Exception {
        // Arrange
        TicketResponseDTO t1 = new TicketResponseDTO(1L, "123", BookingStatus.HOLD, BigDecimal.valueOf(100), "12A", SeatClass.BUSINESS, null, null);
        TicketResponseDTO t2 = new TicketResponseDTO(2L, "456", BookingStatus.CONFIRMED, BigDecimal.valueOf(200), "14C", SeatClass.ECONOMY, null, null);

        when(ticketService.getAllTickets()).thenReturn(List.of(t1, t2));

        // Act + Assert
        mockMvc.perform(get("/api/ticket"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.length()").value(2),
                        jsonPath("$[0].id").value(1),
                        jsonPath("$[0].code").value("123"),
                        jsonPath("$[1].id").value(2),
                        jsonPath("$[1].code").value("456")
                );

        verify(ticketService).getAllTickets();
        verifyNoMoreInteractions(ticketService);
    }

    @Tag("get")
    @Test
    void getAllTickets_shouldReturn200_whenNoRecordsFound() throws Exception {
        // Arrange
        when(ticketService.getAllTickets()).thenReturn(List.of());

        // Act + Assert
        mockMvc.perform(get("/api/ticket"))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.length()").value(0)
                );

        verify(ticketService).getAllTickets();
        verifyNoMoreInteractions(ticketService);
    }

    // ~~~~~~~~~~~~~~~~ UPDATE ~~~~~~~~~~~~~~~~

    @Tag("update")
    @Tag("happy-path")
    @Test
    void updateTicket_shouldReturn200AndResponseBody() throws Exception {
        // Arrange
        Long id = 1L;
        String requestBody = createRequestBody(2L, 3L, BigDecimal.valueOf(150));

        TicketResponseDTO updated = new TicketResponseDTO(id, "UPD123", BookingStatus.CONFIRMED, BigDecimal.valueOf(150), "10B", SeatClass.ECONOMY, null, null);

        when(ticketService.updateTicket(eq(id), any(TicketRequestDTO.class))).thenReturn(updated);

        // Act + Assert
        mockMvc.perform(put("/api/ticket/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                        .andExpectAll(
                                status().isOk(),
                                jsonPath("$.id").value(1),
                                jsonPath("$.code").value("UPD123"),
                                jsonPath("$.bookingStatus").value("CONFIRMED"),
                                jsonPath("$.price").value(150),
                                jsonPath("$.seatNumber").value("10B"),
                                jsonPath("$.seatClass").value("ECONOMY")
                        );

        verify(ticketService).updateTicket(eq(id), any(TicketRequestDTO.class));
        verifyNoMoreInteractions(ticketService);
    }

    @Tag("update")
    @Tag("validation")
    @Test
    void updateTicket_shouldReturn400_whenBookingIdIsNull() throws Exception {
        // Arrange
        String requestBody = createRequestBody(null, 1L, BigDecimal.valueOf(100));

        // Act + Assert
        mockMvc.perform(put("/api/ticket/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                        .andExpectAll(
                                status().isBadRequest(),
                                jsonPath("$[0].code").value("NotNull"),
                                jsonPath("$[0].message").value(BOOKING_ID_NOT_NULL)
                        );

        verifyNoInteractions(ticketService);
    }

    @Tag("update")
    @Tag("validation")
    @Test
    void updateTicket_shouldReturn400_whenSeatIdIsNull() throws Exception {
        // Arrange
        String requestBody = createRequestBody(1L, null, BigDecimal.valueOf(100));

        // Act + Assert
        mockMvc.perform(put("/api/ticket/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                        .andExpectAll(
                                status().isBadRequest(),
                                jsonPath("$[0].code").value("NotNull"),
                                jsonPath("$[0].message").value(SEAT_ID_NOT_NULL)
                        );

        verifyNoInteractions(ticketService);
    }

    @Tag("update")
    @Tag("validation")
    @Test
    void updateTicket_shouldReturn400_whenPriceIsNull() throws Exception {
        // Arrange
        String requestBody = createRequestBody(1L, 1L, null);

        // Act + Assert
        mockMvc.perform(put("/api/ticket/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                        .andExpectAll(
                                status().isBadRequest(),
                                jsonPath("$[0].code").value("NotNull"),
                                jsonPath("$[0].message").value(PRICE_NOT_NULL)
                        );

        verifyNoInteractions(ticketService);
    }

    @Tag("update")
    @Tag("validation")
    @Test
    void updateTicket_shouldReturn400_whenPriceIsZero() throws Exception {
        // Arrange
        String requestBody = createRequestBody(1L, 1L, new BigDecimal("0.00"));

        // Act + Assert
        mockMvc.perform(put("/api/ticket/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                        .andExpectAll(
                                status().isBadRequest(),
                                jsonPath("$[0].code").value("DecimalMin"),
                                jsonPath("$[0].message").value(PRICE_MIN_MESSAGE)
                        );

        verifyNoInteractions(ticketService);
    }

    @Tag("update")
    @Tag("validation")
    @Test
    void updateTicket_shouldReturn400_whenPriceHasTooManyFractionDigits() throws Exception {
        // Arrange
        String requestBody = createRequestBody(1L, 1L, new BigDecimal("100.123"));

        // Act + Assert
        mockMvc.perform(put("/api/ticket/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                        .andExpectAll(
                                status().isBadRequest(),
                                jsonPath("$[0].code").value("Digits"),
                                jsonPath("$[0].message").value(PRICE_DIGITS_MESSAGE)
                        );

        verifyNoInteractions(ticketService);
    }

    @Tag("update")
    @Tag("validation")
    @Test
    void updateTicket_shouldReturn400_whenPriceHasTooManyIntegerDigits() throws Exception {
        // Arrange
        String requestBody = createRequestBody(1L, 1L, new BigDecimal("12345678901.00"));

        // Act + Assert
        mockMvc.perform(put("/api/ticket/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                        .andExpectAll(
                                status().isBadRequest(),
                                jsonPath("$[0].code").value("Digits"),
                                jsonPath("$[0].message").value(PRICE_DIGITS_MESSAGE)
                        );

        verifyNoInteractions(ticketService);
    }

    // ~~~~~~~~~~~~~~~~ DELETE ~~~~~~~~~~~~~~~~

    @Tag("delete")
    @Tag("happy-path")
    @Test
    void deleteTicket_shouldReturn204() throws Exception {
        // Arrange
        doNothing().when(ticketService).deleteTicket(1L);

        // Act + Assert
        mockMvc.perform(delete("/api/ticket/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(ticketService).deleteTicket(1L);
        verifyNoMoreInteractions(ticketService);
    }

     @Tag("delete")
     @Tag("validation")
     @Test
     void deleteTicket_shouldReturn404_whenTicketNotFound() throws Exception {
         doThrow(new BusinessException(List.of(new ErrorModel(ErrorCode.TICKET_NOT_FOUND))))
                 .when(ticketService).deleteTicket(999L);

         mockMvc.perform(delete("/api/ticket/{id}", 999L))
                 .andExpect(status().isNotFound());

         verify(ticketService).deleteTicket(999L);
         verifyNoMoreInteractions(ticketService);
     }

}