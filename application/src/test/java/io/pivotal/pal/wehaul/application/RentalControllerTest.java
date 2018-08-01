package io.pivotal.pal.wehaul.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.pivotal.pal.wehaul.domain.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class RentalControllerTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private RentalService mockRentalService;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        RentalController rentalController = new RentalController(mockRentalService);
        mockMvc = MockMvcBuilders.standaloneSetup(rentalController).build();
    }

    @Test
    public void createRental() throws Exception {
        RentalController.CreateRentalDto requestDto = new RentalController.CreateRentalDto("some-customer-name", "SMALL");
        String requestBody = objectMapper.writeValueAsString(requestDto);


        mockMvc
                .perform(
                        post("/rentals")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(requestBody)
                )
                .andExpect(status().isOk());


        verify(mockRentalService).create("some-customer-name", TruckSize.SMALL);
    }

    @Test
    public void pickUpRental() throws Exception {
        mockMvc
                .perform(
                        post("/rentals/00000000-0000-0000-0000-000000000000/pick-up")
                )
                .andExpect(status().isOk());


        verify(mockRentalService).pickUp(ConfirmationNumber.of("00000000-0000-0000-0000-000000000000"));
    }

    @Test
    public void dropOffRental() throws Exception {
        RentalController.DropOffRentalDto requestDto = new RentalController.DropOffRentalDto(500);
        String requestBody = objectMapper.writeValueAsString(requestDto);


        mockMvc
                .perform(
                        post("/rentals/00000000-0000-0000-0000-000000000000/drop-off")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(requestBody)
                )
                .andExpect(status().isOk());


        verify(mockRentalService).dropOff(ConfirmationNumber.of("00000000-0000-0000-0000-000000000000"), 500);
    }

    @Test
    public void getAllRentals() throws Exception {
        Rental rental1 = new Rental("some-customer-name-1", Vin.of("test-0001"));
        Rental rental2 = new Rental("some-customer-name-2", Vin.of("test-0002"));
        List<Rental> rentals = Arrays.asList(rental1, rental2);
        when(mockRentalService.findAll()).thenReturn(rentals);

        RentalController.RentalDto rentalDto1 = new RentalController.RentalDto(
                rental1.getConfirmationNumber().getConfirmationNumber().toString(),
                "some-customer-name-1",
                "test-0001",
                null
        );
        RentalController.RentalDto rentalDto2 = new RentalController.RentalDto(
                rental2.getConfirmationNumber().getConfirmationNumber().toString(),
                "some-customer-name-2",
                "test-0002",
                null
        );
        List<RentalController.RentalDto> rentalDtos = Arrays.asList(rentalDto1, rentalDto2);
        String expectedResponseBody = objectMapper.writeValueAsString(rentalDtos);


        mockMvc
                .perform(
                        get("/rentals")
                )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseBody));


        verify(mockRentalService).findAll();
    }
}
