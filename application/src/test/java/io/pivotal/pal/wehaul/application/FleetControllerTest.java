package io.pivotal.pal.wehaul.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.pivotal.pal.wehaul.domain.FleetService;
import io.pivotal.pal.wehaul.domain.Truck;
import io.pivotal.pal.wehaul.domain.TruckSize;
import io.pivotal.pal.wehaul.domain.Vin;
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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class FleetControllerTest {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private FleetService mockFleetService;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        FleetController fleetController = new FleetController(mockFleetService);
        mockMvc = MockMvcBuilders.standaloneSetup(fleetController).build();
    }

    @Test
    public void buyTruck() throws Exception {
        FleetController.BuyTruckDto requestDto =
                new FleetController.BuyTruckDto("some-vin", 1000, 25);
        String requestBody = objectMapper.writeValueAsString(requestDto);


        mockMvc
                .perform(
                        post("/fleet/trucks")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(requestBody)
                )
                .andExpect(status().isOk());


        verify(mockFleetService).buyTruck(Vin.of("some-vin"), 1000, 25);
    }

    @Test
    public void getAllTrucks() throws Exception {
        Truck truck1 = new Truck(Vin.of("some-vin-1"), 1000, TruckSize.LARGE, 25);
        Truck truck2 = new Truck(Vin.of("some-vin-2"), 2000, TruckSize.SMALL, 15);
        List<Truck> trucks = Arrays.asList(truck1, truck2);
        when(mockFleetService.findAll()).thenReturn(trucks);

        FleetController.TruckDto truckDto1 = new FleetController.TruckDto("some-vin-1", "RENTABLE", 1000, "LARGE", 25);
        FleetController.TruckDto truckDto2 = new FleetController.TruckDto("some-vin-2", "RENTABLE", 2000, "SMALL", 15);
        List<FleetController.TruckDto> truckDtos = Arrays.asList(truckDto1, truckDto2);
        String expectedResponseBody = objectMapper.writeValueAsString(truckDtos);


        mockMvc
                .perform(
                        get("/fleet/trucks")
                )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseBody));


        verify(mockFleetService).findAll();
    }

    @Test
    public void sendForInspection() throws Exception {
        mockMvc
                .perform(
                        post("/fleet/trucks/some-vin/send-for-inspection")
                )
                .andExpect(status().isOk());


        verify(mockFleetService).sendForInspection(Vin.of("some-vin"));
    }

    @Test
    public void returnFromInspection() throws Exception {
        FleetController.ReturnFromInspectionDto requestDto =
                new FleetController.ReturnFromInspectionDto("some-notes", 2000);
        String requestBody = objectMapper.writeValueAsString(requestDto);


        mockMvc
                .perform(
                        post("/fleet/trucks/some-vin/return-from-inspection")
                                .contentType(MediaType.APPLICATION_JSON_UTF8)
                                .content(requestBody)
                )
                .andExpect(status().isOk());


        verify(mockFleetService).returnFromInspection(Vin.of("some-vin"), "some-notes", 2000);
    }
}
