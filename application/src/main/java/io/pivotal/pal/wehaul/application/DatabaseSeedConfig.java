package io.pivotal.pal.wehaul.application;

import io.pivotal.pal.wehaul.domain.Truck;
import io.pivotal.pal.wehaul.domain.TruckRepository;
import io.pivotal.pal.wehaul.domain.TruckSize;
import io.pivotal.pal.wehaul.domain.Vin;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DatabaseSeedConfig {

    private final TruckRepository truckRepository;

    public DatabaseSeedConfig(TruckRepository truckRepository) {
        this.truckRepository = truckRepository;
    }

    @PostConstruct
    public void populateDatabase() {
        Truck truck = new Truck(
                Vin.of("test-0001"),
                0,
                TruckSize.LARGE,
                25
        );
        truck.sendForInspection();
        truckRepository.save(truck);

        Truck truck2 = new Truck(
                Vin.of("test-0002"),
                0,
                TruckSize.SMALL,
                15
        );
        truckRepository.save(truck2);
    }
}
