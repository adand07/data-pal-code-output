package io.pivotal.pal.wehaul.domain;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TruckSizeChartTest {

    private TruckSizeChart truckSizeChart;

    @Before
    public void setUp() {
        truckSizeChart = new TruckSizeChart();
    }

    @Test
    public void getSizeByTruckLength_smallTruck() {
        TruckSize truckSize = truckSizeChart.getSizeByTruckLength(19);


        assertThat(truckSize).isEqualTo(TruckSize.SMALL);
    }

    @Test
    public void getSizeByTruckLength_largeTruck() {
        TruckSize truckSize = truckSizeChart.getSizeByTruckLength(20);


        assertThat(truckSize).isEqualTo(TruckSize.LARGE);
    }
}
