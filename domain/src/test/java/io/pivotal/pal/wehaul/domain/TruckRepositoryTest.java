package io.pivotal.pal.wehaul.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext
@RunWith(SpringRunner.class)
@DataJpaTest
public class TruckRepositoryTest {

    @Autowired
    private TruckRepository truckRepository;

    @Test
    public void findAllByTruckSizeAndStatus() {
        Truck truck1 = new Truck(Vin.of("test-0001"), 1000, TruckSize.LARGE, null);
        truck1.sendForInspection();
        Truck truck2 = new Truck(Vin.of("test-0002"), 2000, TruckSize.LARGE, null);
        truck2.sendForInspection();
        Truck truck3 = new Truck(Vin.of("test-0003"), 3000, TruckSize.LARGE, null);
        Truck truck4 = new Truck(Vin.of("test-0004"), 3000, TruckSize.SMALL, null);
        truckRepository.save(Arrays.asList(truck1, truck2, truck3, truck4));


        List<Truck> trucks = truckRepository.findAllByTruckSizeAndStatus(TruckSize.LARGE, TruckStatus.RENTABLE);


        assertThat(trucks)
                .usingFieldByFieldElementComparator()
                .containsExactly(truck3);
    }

    @Test
    public void findAllByTruckSizeAndStatus_noneFound() {
        Truck truck1 = new Truck(Vin.of("test-0001"), 1000, null, null);
        truck1.sendForInspection();
        truckRepository.save(truck1);


        List<Truck> trucks = truckRepository.findAllByTruckSizeAndStatus(TruckSize.LARGE, TruckStatus.RENTABLE);


        assertThat(trucks).isEmpty();
    }

    @EnableAutoConfiguration
    @SpringBootConfiguration
    static class ContextConfig {
    }
}
