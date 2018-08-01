package io.pivotal.pal.wehaul.domain;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FleetServiceTest {

    @Mock
    private TruckSizeChart mockTruckSizeChart;
    @Mock
    private TruckRepository mockTruckRepository;
    @Mock
    private TruckInspectionRepository mockTruckInspectionRepository;
    @Captor
    private ArgumentCaptor<Truck> truckCaptor;
    @Captor
    private ArgumentCaptor<TruckInspection> truckInspectionCaptor;

    private FleetService fleetService;

    @Before
    public void setUp() {
        fleetService = new FleetService(
                mockTruckSizeChart,
                mockTruckRepository,
                mockTruckInspectionRepository
        );
    }

    @Test
    public void buyTruck() {
        when(mockTruckSizeChart.getSizeByTruckLength(anyInt())).thenReturn(TruckSize.LARGE);


        fleetService.buyTruck(Vin.of("some-vin"), 1000, 25);


        InOrder inOrder = inOrder(mockTruckSizeChart, mockTruckRepository);
        inOrder.verify(mockTruckSizeChart).getSizeByTruckLength(25);
        inOrder.verify(mockTruckRepository).save(truckCaptor.capture());

        Truck savedTruck = truckCaptor.getValue();
        assertThat(savedTruck.getVin()).isEqualTo(Vin.of("some-vin"));
        assertThat(savedTruck.getOdometerReading()).isEqualTo(1000);
        assertThat(savedTruck.getTruckSize()).isEqualTo(TruckSize.LARGE);
        assertThat(savedTruck.getTruckLength()).isEqualTo(25);
    }

    @Test
    public void sendForInspection() {
        Truck mockTruck = mock(Truck.class);
        when(mockTruckRepository.findOne(any())).thenReturn(mockTruck);
        Vin vin = Vin.of("some-vin");


        fleetService.sendForInspection(vin);


        InOrder inOrder = inOrder(mockTruck, mockTruckRepository);
        inOrder.verify(mockTruckRepository).findOne(vin);
        inOrder.verify(mockTruck).sendForInspection();
        inOrder.verify(mockTruckRepository).save(mockTruck);

        verifyNoMoreInteractions(mockTruck);
    }

    @Test
    public void returnFromInspection() {
        Truck mockTruck = mock(Truck.class);
        when(mockTruckRepository.findOne(any())).thenReturn(mockTruck);
        Vin vin = Vin.of("some-vin");
        String notes = "some-notes";
        int odometerReading = 2;


        fleetService.returnFromInspection(vin, notes, odometerReading);


        InOrder inOrder = inOrder(mockTruck, mockTruckRepository, mockTruckInspectionRepository);
        inOrder.verify(mockTruckRepository).findOne(vin);
        inOrder.verify(mockTruck).returnFromInspection(odometerReading);
        inOrder.verify(mockTruckRepository).save(mockTruck);
        inOrder.verify(mockTruckInspectionRepository).save(truckInspectionCaptor.capture());

        TruckInspection createdEntry = truckInspectionCaptor.getValue();
        assertThat(createdEntry.getTruckVin()).isEqualTo(vin);
        assertThat(createdEntry.getOdometerReading()).isEqualTo(odometerReading);
        assertThat(createdEntry.getNotes()).isEqualTo(notes);

        verifyNoMoreInteractions(mockTruck);
    }

    @Test
    public void findAll() {
        Truck mockTruck1 = mock(Truck.class);
        Truck mockTruck2 = mock(Truck.class);
        List<Truck> toBeReturned = Arrays.asList(mockTruck1, mockTruck2);
        when(mockTruckRepository.findAll()).thenReturn(toBeReturned);


        Collection<Truck> trucks = fleetService.findAll();


        assertThat(trucks).hasSameElementsAs(toBeReturned);
        verify(mockTruckRepository).findAll();
    }

    @Test
    public void sendForInspection_whenNoTruckFound() {
        Vin vin = Vin.of("cant-find-me");


        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fleetService.sendForInspection(vin))
                .withMessage(String.format("No truck found with VIN=%s", vin));


        verify(mockTruckRepository).findOne(vin);
        verifyNoMoreInteractions(mockTruckRepository);
    }

    @Test
    public void returnFromInspection_whenNoTruckFound() {
        Vin vin = Vin.of("cant-find-me");


        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> fleetService.returnFromInspection(vin, "some-notes", 5000))
                .withMessage(String.format("No truck found with VIN=%s", vin));


        verify(mockTruckRepository).findOne(vin);
        verifyNoMoreInteractions(mockTruckRepository);
    }
}
