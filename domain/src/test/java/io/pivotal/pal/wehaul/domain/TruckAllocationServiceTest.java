package io.pivotal.pal.wehaul.domain;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TruckAllocationServiceTest {

    @Mock
    private TruckRepository mockTruckRepository;

    private TruckAllocationService service;

    @Before
    public void setUp() {
        service = new TruckAllocationService(mockTruckRepository);
    }

    @Test
    public void allocateTruck() {
        Truck expected = mock(Truck.class);
        when(mockTruckRepository.findAllByTruckSizeAndStatus(any(), any()))
                .thenReturn(Arrays.asList(expected, mock(Truck.class), mock(Truck.class)));


        Truck truck = service.allocateTruck(TruckSize.LARGE);


        assertThat(truck).isEqualTo(expected);

        verify(mockTruckRepository).findAllByTruckSizeAndStatus(TruckSize.LARGE, TruckStatus.RENTABLE);
    }

    @Test
    public void allocateTruck_noAvailableTrucks() {
        when(mockTruckRepository.findAllByTruckSizeAndStatus(any(), any()))
                .thenReturn(emptyList());


        assertThatExceptionOfType(IllegalStateException.class)
                .isThrownBy(() -> service.allocateTruck(TruckSize.LARGE))
                .withMessage("No trucks available to rent");
    }
}
