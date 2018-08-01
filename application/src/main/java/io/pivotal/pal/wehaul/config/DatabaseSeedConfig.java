package io.pivotal.pal.wehaul.config;

import io.pivotal.pal.wehaul.fleet.domain.FleetTruck;
import io.pivotal.pal.wehaul.fleet.domain.FleetTruckRepository;
import io.pivotal.pal.wehaul.fleet.domain.MakeModel;
import io.pivotal.pal.wehaul.rental.domain.RentalTruck;
import io.pivotal.pal.wehaul.rental.domain.RentalTruckRepository;
import io.pivotal.pal.wehaul.rental.domain.RentalTruckSize;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class DatabaseSeedConfig {

    private final FleetTruckRepository fleetTruckRepository;
    private final RentalTruckRepository rentalTruckRepository;

    public DatabaseSeedConfig(FleetTruckRepository fleetTruckRepository,
                              RentalTruckRepository rentalTruckRepository) {
        this.fleetTruckRepository = fleetTruckRepository;
        this.rentalTruckRepository = rentalTruckRepository;
    }

    @PostConstruct
    public void populateDatabase() {
        // Create one Truck in both Fleet + Rental perspectives that is unrentable/in inspection
        String vin = "test-0001";
        FleetTruck inInspectionFleetTruck =
                new FleetTruck(vin, 0, new MakeModel("TruckCo", "The Big One"));
        fleetTruckRepository.save(inInspectionFleetTruck);

        RentalTruck unrentableRentalTruck = new RentalTruck(vin, RentalTruckSize.LARGE);
        unrentableRentalTruck.preventRenting();
        rentalTruckRepository.save(unrentableRentalTruck);


        // Create another Truck in both Fleet + Rental perspectives that is rentable/not in inspection
        String vin2 = "test-0002";
        FleetTruck inspectableFleetTruck =
                new FleetTruck(vin2, 0, new MakeModel("TruckCo", "The Small One"));
        inspectableFleetTruck.returnFromInspection("some notes", 0);
        fleetTruckRepository.save(inspectableFleetTruck);

        RentalTruck rentableRentalTruck = new RentalTruck(vin2, RentalTruckSize.SMALL);
        rentalTruckRepository.save(rentableRentalTruck);
    }
}